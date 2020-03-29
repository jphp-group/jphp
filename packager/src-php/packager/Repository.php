<?php

namespace packager;

use compress\GzipInputStream;
use compress\GzipOutputStream;
use compress\TarArchive;
use compress\TarArchiveEntry;
use packager\cli\Console;
use packager\repository\ExternalRepository;
use packager\repository\GithubReleasesRepository;
use packager\repository\GithubRepository;
use packager\repository\GitRepository;
use packager\repository\LocalDirRepository;
use packager\repository\ServerRepository;
use packager\repository\SingleExternalRepository;
use php\format\JsonProcessor;
use php\format\ProcessorException;
use php\io\File;
use php\io\IOException;
use php\io\Stream;
use php\lang\IllegalArgumentException;
use php\lib\arr;
use php\lib\fs;
use php\lib\reflect;
use php\lib\str;
use php\time\Time;
use php\time\Timer;
use php\util\SharedValue;
use semver\SemVersion;
use function _print;
use function var_dump;

/**
 * Class Repository
 * @package packager
 */
class Repository
{
    private static $externalRepoClasses = [];

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
     * @var array
     */
    private $sessionCache = [];


    private $lock;

    /**
     * Repository constructor.
     * @param string $directory
     */
    public function __construct(string $directory)
    {
        $this->lock = new SharedValue();

        $this->dir = $directory;

        if (fs::isFile("$directory/cache.json")) {
            try {
                $this->cache = fs::parseAs("$directory/cache.json", "json");
            } catch (IOException | ProcessorException $e) {
                Console::warn("Failed to load repo cache, {0}", $e->getMessage());
                $this->cache = [];
            }
        }

        $this->addExternalRepoByString("jphp");
        //$this->addExternalRepoByString("central");
    }

    /**
     * @return string
     */
    public function getDirectory(): string
    {
        return $this->dir;
    }

    /**
     *
     */
    protected function saveCache()
    {
        try {
            Console::debug("Repository.SaveCache file={0}", "$this->dir/cache.json");

            fs::formatAs("$this->dir/cache.json", $this->cache, 'json', JsonProcessor::SERIALIZE_PRETTY_PRINT);
        } catch (IOException $e) {
            // nop.
        }
    }

    /**
     * @param ExternalRepository $repository
     * @param string $pkgName
     * @param bool $cached
     * @return array
     */
    protected function getVersionsFromExternal(ExternalRepository $repository, string $pkgName, bool $cached = true): array
    {
        Console::debug("Repository.GetVersionsFromExternal repo={0} pkg={1} cached={2}", $repository->getSource(), $pkgName, $cached ? 'yes' : 'no');

        $cache = $this->cache['external'][$repository->getSource()][$pkgName];

        if ($cached && $repository->isNeedCache()) {
            if ($cache !== null && is_array($cache['versions']) && $cache['time'] > Time::millis() - Timer::parsePeriod($repository->getCacheTime())) {
                return $cache['versions'];
            }
        }

        $cache = $this->sessionCache['external'][$repository->getSource()][$pkgName];
        if ($cache !== null && is_array($cache['versions'])) {
            return $cache['versions'];
        }

        Console::log("-> get versions of package {0}, source: {1}", $pkgName, $repository->getSource());

        try {
            $versions = $repository->getVersions($pkgName);

            $this->lock->synchronize(function () use ($repository, $pkgName, $versions) {
                $cache = [
                    'versions' => $versions,
                    'time' => Time::millis()
                ];

                $this->sessionCache['external'][$repository->getSource()][$pkgName] = $cache;

                if ($repository->isNeedCache()) {
                    $this->cache['external'][$repository->getSource()][$pkgName] = $cache;

                    $this->saveCache();
                }
            });

            return (array)$versions;
        } catch (IOException|ProcessorException $e) {
            $this->lock->synchronize(function () use ($repository, $pkgName) {
                $this->sessionCache['external'][$repository->getSource()][$pkgName]['versions'] = $this->cache['external'][$repository->getSource()][$pkgName]['versions'] ?: [];
                $this->sessionCache['external'][$repository->getSource()][$pkgName]['time'] = Time::millis();

                if ($repository->isNeedCache()) {
                    $this->cache['external'][$repository->getSource()][$pkgName]['versions'] = $this->cache['external'][$repository->getSource()][$pkgName]['versions'] ?: [];
                    $this->cache['external'][$repository->getSource()][$pkgName]['time'] = Time::millis();
                }

                $this->saveCache();
            });

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

        if ($result) {
            $result['repo'] = $repository->getSource();
        }

        return $result;
    }

    /**
     * @param string $pkgName
     * @param string $version
     * @return array|null
     */
    protected function getVersionInfo(string $pkgName, string $version): ?array
    {
        Console::debug("Repository.GetVersionInfo pkg={0}@{1}", $pkgName, $version);

        if (fs::isFile($file = "$this->dir/$pkgName/$version.json")) {
            return fs::parse($file);
        }

        return null;
    }

    /**
     * @param ExternalRepository $repository
     * @return ExternalRepository
     */
    public function addExternalRepo(ExternalRepository $repository)
    {
        Console::debug("Repository.AddExternalRepo repo='{0}' type='{1}'", $repository->getSource(), reflect::typeOf($repository));

        $this->externals[$repository->getSource()] = $repository;
        return $repository;
    }

    /**
     * @param string $repo
     * @return ExternalRepository
     */
    public function addExternalRepoByString(string $repo)
    {
        if ($repo === "jphp") {
            //$this->addExternalRepo(new GithubReleasesRepository("https://github.com/jphp-compiler/jphp-repo/releases"));
            return $this->addExternalRepo(new ServerRepository("http://api.develnext.org"));
        }

        if ($repo === "central") {
            return $this->addExternalRepo(new GithubRepository('https://github.com/jphp-compiler/central-repo'));
        }

        $classes = Repository::getExternalRepoClasses();

        foreach ($classes as $class) {
            /** @var ExternalRepository $r */
            $r = new $class($repo);
            if ($r instanceof ExternalRepository && !($r instanceof SingleExternalRepository) && $r->isFit()) {
                return $this->addExternalRepo($r);
            }
        }

        Console::warn("Failed to use the '{0}' repository, it can not be recognized.", $repo);
        return null;
    }

    /**
     * @param string $name
     * @param bool $onlyLocal
     * @param bool $cached
     * @return array
     */
    public function getPackageVersions(string $name, bool $onlyLocal = true, bool $cached = true): array
    {
        Console::debug("Repository.GetPackageVersions pkg={0} onlyLocal={1} cached={2}", $name, $onlyLocal ? 'yes' : 'no', $cached ? 'yes' : 'no');

        $dir = "$this->dir/$name/";

        $versions = fs::scan($dir, ['excludeFiles' => true], 1);

        foreach ($versions as &$version) {
            $version = fs::name($version);
        }

        $versions = arr::combine($versions, $versions);

        if (!$onlyLocal) {
            foreach ($this->externals as $external) {
                foreach ($this->getVersionsFromExternal($external, $name, $cached) as $version => $info) {
                    if (!$versions[$version]) {
                        $versions[$version] = $external;
                    } else {
                        $externalInfo = $this->getVersionInfoFromExternal($external, $name, $version);
                        $localInfo = $this->getVersionInfo($name, $version);

                        if ($externalInfo != null && $localInfo != null) {
                            if (isset($externalInfo['hash'])) {
                                if ($externalInfo['hash'] !== $localInfo['hash']) {
                                    $versions[$version] = $external;
                                }
                            } else {
                                if ($externalInfo['size'] !== $localInfo['size'] || $externalInfo['sha256'] !== $localInfo['sha256']) {
                                    $versions[$version] = $external;
                                }
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
     * @param bool $cached
     * @return null|Package
     */
    public function findPackage(string $name, string $versionPattern, ?PackageLock $lock = null, bool $cached = true): ?Package
    {
        Console::debug(
            "Repository.FindPackage pkg={0}@{1} lock={2} cached={3}",
            $name, $versionPattern, $lock ? 'exists' : 'none', $cached ? 'yes' : 'no'
        );

        if ($lock) {
            $lockVersion = $lock->findVersion($name);

            if ($lockVersion) {
                try {
                    if ($lockVersion === $versionPattern || (new SemVersion($lockVersion))->satisfies($versionPattern)) {
                        $versionPattern = $lockVersion;
                    }
                } catch (\Throwable $e) {
                    // nop.
                }
            }
        }

        $foundVersion = $foundVersionSource = null;
        $complex = false;
        $found = false;

        $versions = [];

        foreach (static::$externalRepoClasses as $externalRepoClass) {
            $repo = new $externalRepoClass($versionPattern);

            if ($repo instanceof SingleExternalRepository && $repo->isFit()) {
                $found = true;
                $versions = $this->getPackageVersions($name, true, $cached);

                $versionPattern = $repo->getHash();
                $complex = true;

                if (!$versions[$versionPattern]) {
                    $versions[$versionPattern] = $repo;
                } else {
                    $oldInfo = $this->getVersionInfo($name, $versionPattern);

                    $newInfo = $cached ? $this->getVersionInfoFromExternal($repo, $name, $versionPattern) : null;

                    if (!$newInfo) {
                        try {
                            $newInfo = $repo->getVersions($name)[$versionPattern];
                        } catch (\Throwable $e) {
                            Console::warn($e->getMessage());
                            continue;
                        }
                    }

                    if ($oldInfo['hash'] !== $newInfo['hash']) {
                        $versions[$versionPattern] = $repo;
                    }
                }
                break;
            }
        }

        if (!$found) {
            $versions = $this->getPackageVersions($name, false, $cached);
        }

        if (!$foundVersion) {
            $foundVersions = [];

            foreach ($versions as $version => $source) {
                if ($version === $versionPattern) {
                    $foundVersions[$version] = $source;
                    continue;
                }

                try {
                    try {
                        $semVer = new SemVersion($version);
                    } catch (IllegalArgumentException $e) {
                        continue;
                    }

                    if ($semVer->satisfies($versionPattern === '' ? '*' : $versionPattern)) {
                        $foundVersions[$version] = $source;
                    }
                } catch (\Exception $e) {
                    if (!$complex) {
                        unset($foundVersions[$version]);
                        Console::warn("Invalid version '{0}@{1}', pattern is '{2}', {3}, ignored.", $name, $version, $versionPattern, $e->getMessage());
                    }
                }
            }

            $foundVersions = arr::sortByKeys($foundVersions, function ($a, $b) {
                return new SemVersion($a) <=> new SemVersion($b);
            }, true);
            $foundVersion = arr::lastKey($foundVersions);
            $foundVersionSource = arr::last($foundVersions);
        }

        if ($foundVersion) {
            if ($foundVersionSource instanceof ExternalRepository) {
                $foundVersionInfo = $this->getVersionInfoFromExternal($foundVersionSource, $name, $foundVersion);

                Console::info("-> ".Colors::withColor('Download Package', 'green')." {0}@{1} from '{$foundVersionSource->getSource()}'", $name, $foundVersion);

                $indexFile = "$this->dir/$name/$foundVersion.json";
                $archFile = "$this->dir/$name/$foundVersion.tar.gz";
                $dirFile = "$this->dir/$name/$foundVersion";
                fs::ensureParent($archFile);

                try {
                    if ($foundVersionSource->downloadTo($name, $foundVersion, $archFile)) {
                        $this->installFromArchive($archFile);
                        fs::delete($archFile);
                        fs::format($indexFile, $foundVersionInfo, JsonProcessor::SERIALIZE_PRETTY_PRINT);
                    } else if ($foundVersionSource->downloadToDirectory($name, $foundVersion, $dirFile)) {
                        fs::format($indexFile, $foundVersionInfo, JsonProcessor::SERIALIZE_PRETTY_PRINT);
                    }
                } catch (\Throwable $e) {
                    Console::error($e->getMessage());
                    return null;
                }
            }

            $pkg = $this->getPackage($name, "$foundVersion");

            return $pkg;
        }

        return null;
    }

    public function getPackageArchiveFile(string $name, string $version): ?File
    {
        if (fs::isFile($archFile = "$this->dir/$name/$version.tar.gz")) {
            return new File($archFile);
        }

        return null;
    }

    /**
     * @param string $name
     * @param string $version
     * @param array $info
     * @return null|Package
     */
    public function getPackage(string $name, string $version, array $info = []): ?Package
    {
        $file = "$this->dir/$name/$version/" . Package::FILENAME;
        $infoFile = "$this->dir/$name/$version.json";

        if (!fs::isFile($file)) {
            return null;
        }

        $oInfo = fs::isFile($infoFile) ? fs::parse($infoFile) : [];

        return $this->readPackage($file, flow($oInfo, $info)->toMap());
    }

    /**
     * @param Package $package
     * @param string $vendorDir
     */
    public function copyTo(Package $package, string $vendorDir)
    {
        fs::makeDir($vendorDir);

        $dir = fs::normalize("$this->dir/{$package->getName()}/{$package->getRealVersion()}/");

        fs::clean("$vendorDir/{$package->getName()}");

        fs::scan($dir, function ($filename) use ($vendorDir, $dir, $package) {
            $relName = str::sub($filename, str::length($dir) + 1);

            if (fs::isDir($filename)) {
                fs::makeDir("$vendorDir/{$package->getName()}/$relName");
            } else {
                fs::makeDir("$vendorDir/{$package->getName()}/");
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
        $path = "$this->dir/{$package->getName()}/{$package->getRealVersion()}";

        if (fs::isDir($path)) {
            $archFile = new File("$path.tar.gz");

            $arch = new TarArchive(new GzipOutputStream("$path.tar.gz", ['compressLevel' => 9]));
            $arch->open();

            foreach (arr::sort(fs::scan($path)) as $file) {
                if (fs::isFile($file)) {
                    $name = fs::relativize($file, $path);
                    if ($name === "README.md") {
                        continue;
                    }

                    $arch->addFile($file, $name);
                }
            }

                $arch->close();

            return $archFile;
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
            $ignore = $package->fetchIgnore();

            $destDir = fs::normalize("$this->dir/{$package->getName()}/{$package->getRealVersion()}");

            fs::clean($destDir);
            fs::makeDir($destDir);

            $directory = fs::normalize($directory);

            fs::scan($directory, function ($filename) use ($destDir, $directory, $ignore) {
                $relName = fs::relativize($filename, $directory);

                if ($ignore->test($relName)) {
                    return;
                }

                if (fs::isDir($filename)) {
                    // skip.
                } else {
                    fs::ensureParent("$destDir/$relName");
                    fs::copy($filename, "$destDir/$relName", null, 1024 * 256);
                }
            });
        }
    }

    /**
     * Install package from tar.gz archive.
     * @param string $archFile
     * @return bool
     */
    public function installFromArchive(string $archFile): bool
    {
        $arch = new TarArchive(new GzipInputStream($archFile));

        /** @var Package $package */
        $package = null;

        $entry = $arch->read(Package::FILENAME, function ($stat, Stream $stream) use (&$package, $archFile) {
            $package = $this->readPackage($stream);
        });

        if ($entry) {
            $dir = "$this->dir/{$package->getName()}/{$package->getRealVersion()}";

            if (fs::isDir($dir)) {
                fs::clean($dir);
            }

            if (fs::exists($dir)) {
                fs::delete($dir);
            }

            fs::makeDir($dir);

            $arch = new TarArchive(new GzipInputStream($archFile));
            $arch->readAll(function (TarArchiveEntry $entry, ?Stream $stream) use ($dir) {
                if ($entry->isDirectory()) {
                    fs::makeDir("$dir/$entry->name");
                } else {
                    fs::ensureParent("$dir/$entry->name");
                    fs::copy($stream, "$dir/$entry->name", null, 1024 * 128);
                }
            });

            return true;
        }


        return false;
    }

    public function index(string $module, string $destDir = null)
    {
        $this->indexAll($destDir, [$module]);
    }

    public function indexAll(string $destDir = null, array $onlyModules = [])
    {
        $modules = fs::scan($this->dir, ['excludeFiles' => true], 1);

        $name = function ($el) {
            return fs::name($el);
        };

        if ($destDir === null) {
            $destDir = $this->dir;
        }

        fs::makeDir($destDir);

        foreach ($modules as $module) {
            if ($onlyModules && !arr::has($onlyModules, fs::name($module))) {
                continue;
            }

            Console::log("Update Index of module ({0})", fs::name($module));

            $module = fs::name($module);

            $index = [];
            if (fs::isFile("$destDir/$module/versions.json")) {
                try {
                    $index = fs::parse("$destDir/$module/versions.json");
                } catch (ProcessorException $e) {
                    $index = [];
                }
            }

            $versions = fs::scan("$this->dir/$module", ['excludeFiles' => true], 1);

            foreach ($versions as $version) {
                $size = 0;
                $hash = '';

                foreach (arr::sort(fs::scan($version)) as $file) {
                    if (fs::isFile($file)) {
                        $mName = fs::relativize($file, $version);

                        if ($mName === "README.md") {
                            $str = "$destDir/$module/" . fs::name($version) . ".md";

                            if (!fs::ensureParent($str)) {
                                Console::warn("Failed to index file {0}", $str);
                                continue;
                            }

                            fs::copy($file, $str);
                        } else {
                            $size += fs::size($file);
                            $hash .= fs::hash($file, 'SHA-256');
                        }
                    }
                }

                $hash = str::hash($hash, 'SHA-256');

                $oldModuleIndex = $index[fs::name($version)];

                if ($oldModuleIndex['size'] === $size && $oldModuleIndex['sha256'] === $hash) {
                    Console::log(" -> Skip version: {0}, size = {1}, hash = {2}", fs::name($version), $size, $hash);
                    continue;
                } else {
                    if ($oldModuleIndex) {
                        Console::log(" -> Update version: {0}, size = {1}, hash = {2}", fs::name($version), $size, $hash);
                    } else {
                        Console::log(" -> Add version: {0}, size = {1}, hash = {2}", fs::name($version), $size, $hash);
                    }
                }

                $archFile = "$destDir/$module/" . fs::name($version) . ".tar.gz";

                fs::delete($archFile);
                if (!fs::isDir(fs::parent($archFile)) && !fs::ensureParent($archFile)) {
                    throw new \Exception("Failed to create directory: " . fs::parent($archFile));
                }

                $arch = new TarArchive(new GzipOutputStream($archFile, ['compressLevel' => 9]));
                $arch->open();
                foreach (arr::sort(fs::scan($version)) as $file) {
                    if (fs::isFile($file)) {
                        $mName = fs::relativize($file, $version);

                        if ($mName === 'README.md') {
                            continue;
                        }

                        $arch->addFile($file, $mName);
                    }
                }
                $arch->close();

                $index[fs::name($version)] = [
                    'size' => $size,
                    'sha256' => $hash,
                ];

                fs::format("$destDir/$module/" . fs::name($version) . ".json", [
                    'size' => $size,
                    'sha256' => $hash
                ]);
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

    /**
     * Delete package from repo.
     *
     * @param Package $oldPkg
     * @return bool
     */
    public function delete(Package $oldPkg)
    {
        return fs::delete("$this->dir/{$oldPkg->getName()}/{$oldPkg->getRealVersion()}");
    }

    /**
     * @param string $name
     * @param callable $callback
     * @return array
     */
    public function deleteByName(string $name, callable $callback = null): array
    {
        $result = [];

        foreach (fs::scan("$this->dir/$name/", ['excludeFiles' => true], 1) as $version) {
            $pkg = $this->getPackage($name, fs::name($version));
            $success = $this->delete($pkg);
            $result[] = $pkg;

            $callback($pkg, $success);
        }

        return $result;
    }

    /**
     * Calc info of pkg directory.
     *
     * @param string $packageDir
     * @param string $algorithm
     * @return array
     */
    public static function calcPackageInfo(string $packageDir, string $algorithm = 'SHA-256')
    {
        $size = 0;
        $hash = '';

        $pkg = Package::readPackage("$packageDir/" . Package::FILENAME);

        $ignore = $pkg->fetchIgnore();

        foreach (arr::sort(fs::scan($packageDir)) as $file) {
            if (fs::isFile($file)) {
                $mName = fs::relativize($file, $packageDir);

                if ($ignore->test($mName)) continue;

                $size += fs::size($file);
                $hash .= fs::hash($file, $algorithm);
            }
        }

        $hash = str::hash($hash, $algorithm);

        $key = str::lower($algorithm);
        $key = str::replace($key, '-', '');

        return [
            'size' => $size,
            $key => $hash
        ];
    }

    /**
     * @return array
     */
    public static function getExternalRepoClasses(): array
    {
        return self::$externalRepoClasses;
    }

    /**
     * @param string $class
     */
    public static function registerExternalRepositoryClass(string $class)
    {
        static::$externalRepoClasses[$class] = $class;
    }

    /**
     *
     */
    public function cleanCache()
    {
        $this->cache = [];
        $this->saveCache();
    }
}