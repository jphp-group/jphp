<?php
namespace packager;
use packager\cli\Console;
use packager\repository\ExternalRepository;
use packager\repository\GithubRepository;
use packager\server\Server;
use php\compress\ZipFile;
use php\format\JsonProcessor;
use php\format\ProcessorException;
use php\io\File;
use php\io\IOException;
use php\io\Stream;
use php\lib\arr;
use php\lib\fs;
use php\lib\str;
use php\time\Time;
use php\util\Regex;
use semver\SemVersion;

/**
 * Class Repository
 * @package packager
 */
class Repository
{
    /**
     * @var string
     */
    private $dir;

    /**
     * @var ExternalRepository[]
     */
    private $externals = [];

    /**
     * @var array
     */
    private $cache = [];

    /**
     * Repository constructor.
     * @param string $directory
     */
    public function __construct(string $directory)
    {
        $this->dir = $directory;
        try {
            $this->cache = fs::parseAs("$directory/cache.json", "json");
        } catch (IOException | ProcessorException $e) {
            $this->cache = [];
        }

        $this->addExternalRepo(new GithubRepository('https://github.com/jphp-compiler/jphp-repo'));
        $this->addExternalRepo(new GithubRepository('https://github.com/jphp-compiler/central-repo'));
    }

    /**
     *
     */
    protected function saveCache()
    {
        try {
            fs::formatAs("$this->dir/cache.json", $this->cache, 'json', JsonProcessor::SERIALIZE_PRETTY_PRINT);
        } catch (IOException $e) {
            // nop.
        }
    }

    /**
     * @param ExternalRepository $repository
     * @param string $pkgName
     * @return array
     */
    protected function getVersionsFromExternal(ExternalRepository $repository, string $pkgName): array
    {
        $cache = $this->cache['external'][$repository->getSource()][$pkgName];

        if ($cache !== null && is_array($cache['versions']) && $cache['time'] > Time::millis() - 1000 * 60 * 10) {
            return $cache['versions'];
        }

        Console::log("-> get versions of package {0}, source: {1}", $pkgName, $repository->getSource());

        try {
            $cache = [
                'versions' => $repository->getVersions($pkgName),
                'time' => Time::millis()
            ];

            $this->cache['external'][$repository->getSource()][$pkgName] = $cache;

            $this->saveCache();

            return (array)$cache['versions'];
        } catch (IOException|ProcessorException $e) {
            $this->cache['external'][$repository->getSource()][$pkgName]['versions'] = $this->cache['external'][$repository->getSource()][$pkgName]['versions'] ?: [];
            $this->cache['external'][$repository->getSource()][$pkgName]['time'] = Time::millis();

            $this->saveCache();

            return [];
            // nop.
        }
    }

    /**
     * @param ExternalRepository $repository
     * @param string $pkgName
     * @param string $version
     * @return array
     */
    protected function getVersionInfoFromExternal(ExternalRepository $repository, string $pkgName, string $version): ?array
    {
        $versions = $this->getVersionsFromExternal($repository, $pkgName);
        $result = $versions[$version];

        $result['repo'] = $repository->getSource();

        return $result;
    }

    /**
     * @param string $pkgName
     * @param string $version
     * @return array|null
     */
    protected function getVersionInfo(string $pkgName, string $version): ?array
    {
        if (fs::isFile($file = "$this->dir/$pkgName/$version.json")) {
            return fs::parse($file);
        }

        return null;
    }

    /**
     * @param ExternalRepository $repository
     */
    public function addExternalRepo(ExternalRepository $repository)
    {
        $this->externals[$repository->getSource()] = $repository;
    }

    /**
     * @param string $name
     * @param bool $onlyLocal
     * @return array
     */
    public function getPackageVersions(string $name, bool $onlyLocal = true): array
    {
        $dir = "$this->dir/$name/";

        $versions = fs::scan($dir, ['excludeFiles' => true], 1);

        foreach ($versions as &$version) {
            $version = fs::name($version);
        }

        $versions = arr::combine($versions, $versions);

        if (!$onlyLocal) {
            foreach ($this->externals as $external) {
                foreach ($this->getVersionsFromExternal($external, $name) as $version => $info) {
                    if (!$versions[$version]) {
                        $versions[$version] = $external;
                    } else {
                        $externalInfo = $this->getVersionInfoFromExternal($external, $name, $version);
                        $localInfo = $this->getVersionInfo($name, $version);

                        if ($externalInfo != null && $localInfo != null) {
                            if ($externalInfo['size'] !== $localInfo['size']
                                || $externalInfo['crc32'] !== $localInfo['crc32']
                                || $externalInfo['sha1'] !== $localInfo['sha1']) {
                                $versions[$version] = $external;
                            }
                        }
                    }
                }
            }
        }

        return $versions;
    }

    /**
     * @param string $name
     * @param string $versionPattern
     * @param null|PackageLock $lock
     * @return null|Package
     */
    public function findPackage(string $name, string $versionPattern, ?PackageLock $lock = null): ?Package
    {
        if ($lock) {
            $lockVersion = $lock->findVersion($name);

            if ($lockVersion) {
                if ($lockVersion === $versionPattern || (new SemVersion($lockVersion))->satisfies($versionPattern)) {
                    $versionPattern = $lockVersion;
                }
            }
        }

        $versions = $this->getPackageVersions($name, false);

        $foundVersions = [];

        foreach ($versions as $version => $source) {
            $semVer = new SemVersion($version);

            if ($version === $versionPattern || $semVer->satisfies($versionPattern)) {
                $foundVersions[$version] = $source;
            }
        }

        $foundVersions = arr::sortByKeys($foundVersions, function ($a, $b) { return new SemVersion($a) <=> new SemVersion($b); }, true);
        $foundVersion = arr::lastKey($foundVersions);
        $foundVersionSource = arr::last($foundVersions);

        if ($foundVersion) {
            if ($foundVersionSource instanceof ExternalRepository) {
                $foundVersionInfo = $this->getVersionInfoFromExternal($foundVersionSource, $name, $foundVersion);

                Console::log("-> download package {0}@{1} from '{$foundVersionSource->getSource()}'", $name, $foundVersion);

                $indexFile = "$this->dir/$name/$foundVersion.json";
                $zipFile = "$this->dir/$name/$foundVersion.zip";
                fs::ensureParent($zipFile);

                if ($foundVersionSource->downloadTo($name, $foundVersion, $zipFile)) {
                    $this->installFromArchive($zipFile);
                    fs::delete($zipFile);
                    fs::format($indexFile, $foundVersionInfo, JsonProcessor::SERIALIZE_PRETTY_PRINT);
                }
            }

            $pkg = $this->getPackage($name, "$foundVersion");
            return $pkg;
        }

        return null;
    }

    /**
     * @param string $name
     * @param string $version
     * @return Package
     */
    public function getPackage(string $name, string $version): ?Package
    {
        $file = "$this->dir/$name/$version/" . Package::FILENAME;
        $infoFile = "$this->dir/$name/$version.json";

        return $this->readPackage($file, fs::isFile($infoFile) ? fs::parse($infoFile) : []);
    }

    /**
     * @param Package $package
     * @param string $vendorDir
     */
    public function copyTo(Package $package, string $vendorDir)
    {
        fs::makeDir($vendorDir);

        $dir = fs::normalize("$this->dir/{$package->getName()}/{$package->getVersion('last')}/");

        fs::clean("$vendorDir/{$package->getName()}");

        fs::scan($dir, function ($filename) use ($vendorDir, $dir, $package) {
            $relName = str::sub($filename, str::length($dir) + 1);

            if (fs::isDir($filename)) {
                fs::makeDir("$vendorDir/{$package->getName()}/$relName");
            } else {
                fs::copy($filename, "$vendorDir/{$package->getName()}/$relName", null, 1024 * 256);
            }
        });
    }

    /**
     * @param string|Stream $source
     * @param array $info
     * @return Package
     */
    public function readPackage($source, array $info = []): Package
    {
        return Package::readPackage($source, $info);
    }

    /**
     * @param Package $package
     * @return null|File
     */
    public function archivePackage(Package $package): ?File
    {
        $path = "$this->dir/{$package->getName()}/{$package->getVersion()}";

        if (fs::isDir($path)) {
            $zipFile = new File("$path.zip");

            if (fs::isFile($zipFile->getPath())) {
                return $zipFile;
            } else {
                $zip = new ZipFile("$path.zip", true);
                $zip->addDirectory($path);
            }

            return $zipFile;
        }

        return null;
    }

    /**
     * Install package from its directory.
     * @param string $directory
     */
    public function installFromDir(string $directory)
    {
        $file = "$directory/" . Package::FILENAME;

        if (fs::isFile($file)) {
            $package = $this->readPackage($file);
            $destDir = fs::normalize("$this->dir/{$package->getName()}/{$package->getVersion('last')}");

            fs::clean($destDir);
            fs::makeDir($destDir);

            $directory = fs::normalize($directory);

            fs::scan($directory, function ($filename) use ($destDir, $directory) {
                $relName = str::sub($filename, str::length($directory) + 1);

                if (fs::isDir($filename)) {
                    fs::makeDir("$destDir/$relName");
                } else {
                    fs::copy($filename, "$destDir/$relName", null, 1024 * 256);
                }
            });
        }
    }

    /**
     * Install package from zip archive.
     * @param string $zipFile
     * @return bool
     */
    public function installFromArchive(string $zipFile): bool
    {
        $zip = new ZipFile($zipFile);
        /** @var Package $package */
        $package = null;

        if ($zip->has(Package::FILENAME)) {
            $zip->read(Package::FILENAME, function (array $stat, Stream $stream) use (&$package, $zipFile) {
                $package = $this->readPackage($stream, [
                    'size' => fs::size($zipFile),
                    'sha1' => fs::hash($zipFile, 'sha1'),
                    'crc32' => (new File($zipFile))->crc32()
                ]);
            });

            $dir = "$this->dir/{$package->getName()}/{$package->getVersion('last')}";

            if (fs::isDir($dir)) {
                fs::clean($dir);
            }

            if (fs::exists($dir)) {
                fs::delete($dir);
            }

            fs::makeDir($dir);
            $zip->unpack($dir);
            return true;
        }

        return false;
    }

    public function indexAll(string $destDir = null)
    {
        $modules = fs::scan($this->dir, ['excludeFiles' => true], 1);

        $name = function ($el) { return fs::name($el); };

        if ($destDir === null) {
            $destDir = $this->dir;
        }

        fs::makeDir($destDir);

        foreach ($modules as $module) {
            Console::log("Update Index of module ({0})", fs::name($module));

            $index = [];
            $module = fs::name($module);

            $versions = fs::scan("$this->dir/$module", ['excludeFiles' => true], 1);

            foreach ($versions as $version) {
                $zipFile = "$destDir/$module/" . fs::name($version) . ".zip";

                fs::delete($zipFile);
                if (!fs::ensureParent($zipFile)) {
                    throw new \Exception("Failed to create directory: " . fs::parent($zipFile));
                }

                $zip = new ZipFile($zipFile, true);
                $zip->addDirectory($version);

                $index[fs::name($version)] = [
                    'size'   => fs::size($zipFile),
                    'sha1'   => fs::hash($zipFile, 'SHA-1'),
                    'crc32'  => (new File($zipFile))->crc32()
                ];
            }

            fs::formatAs(
                "$destDir/$module/versions.json",
                $index,
                'json', JsonProcessor::SERIALIZE_PRETTY_PRINT
            );
        }

        fs::formatAs("$destDir/modules.json", flow($modules)->map($name)->toArray(), 'json', JsonProcessor::SERIALIZE_PRETTY_PRINT);
        Stream::putContents("$destDir/.gitignore", "/*/*/");
    }
}