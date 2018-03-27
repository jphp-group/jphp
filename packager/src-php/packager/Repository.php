<?php
namespace packager;
use packager\cli\Console;
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
use php\util\Regex;

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
     * @var array
     */
    private $externals = [];

    /**
     * Repository constructor.
     * @param string $directory
     */
    public function __construct(string $directory)
    {
        $this->dir = $directory;
        //$this->addExternal('http://jppm.develnext.org');
        $this->addExternal('https://github.com/dim-s/jppm-repo');
        //$this->addExternal('http://localhost:' . Server::PORT);
    }

    /**
     * @param string $server
     */
    public function addExternal(string $server)
    {
        $this->externals[$server] = $server;
    }

    /**
     * @param string $name
     * @return array
     */
    public function getPackageVersions(string $name): array
    {
        $dir = "$this->dir/$name/";

        $versions = fs::scan($dir, ['excludeFiles' => true], 1);

        foreach ($versions as &$version) {
            $version = fs::name($version);
        }

        return $versions;
    }

    /**
     * @param string $name
     * @param array $versions
     * @return array
     */
    public function refreshPackageFromExternal(string $name, array $versions = []): array
    {
        foreach ($this->externals as $external) {
            try {
                if (str::startsWith($external, 'https://github.com/')) {
                    $path = str::sub($external, 19);

                    $externalVersions = fs::parseAs("https://raw.githubusercontent.com/$path/master/$name/versions.json", "json");
                } else {
                    $externalVersions = fs::parseAs("$external/repo/$name/find", "json");
                }

                foreach ($externalVersions as $version) {
                    if (!$versions[$version]) {
                        Console::log("download package {0}@{1} from '$external'", $name, $version);

                        $zipFile = "$this->dir/$name/$version.zip";
                        fs::ensureParent($zipFile);

                        if (str::startsWith($external, 'https://github.com/')) {
                            $copied = fs::copy(
                                "$external/raw/master/$name/$version.zip", $zipFile
                            );
                        } else {
                            $copied = fs::copy(
                                "$external/repo/$name?version=$version&download=1", $zipFile
                            );
                        }

                        if ($copied > 0) {
                            if ($this->installFromArchive($zipFile)) {
                                $versions[$version] = $version;
                            }

                            fs::delete($zipFile);
                        }
                    }
                }
            } catch (ProcessorException|IOException $e) {
                Console::log("Failed to get external ({0}): {1}", $external, $e->getMessage());
            }
        }

        return $versions;
    }

    /**
     * @param string $name
     * @param string $versionPattern
     * @return null|Package
     */
    public function findPackage(string $name, string $versionPattern): ?Package
    {
        $versions = $this->getPackageVersions($name);
        arr::sort($versions);
        $versions = arr::reverse($versions);
        $versions = arr::combine($versions, $versions);

        $versions = $this->refreshPackageFromExternal($name, $versions);

        $versionPattern = str::replace($versionPattern, '.', '\\.');
        $versionPattern = str::replace($versionPattern, '-', '\\-');
        $versionPattern = str::replace($versionPattern, '_', '\\_');
        $versionPattern = str::replace($versionPattern, '*', '[a-z0-9\.\-\_]+');

        $pattern = new Regex("^" . $versionPattern . "$", 'i');

        foreach ($versions as $version) {
            if ($pattern->with($version)->matches()) {
                return $this->getPackage($name, $version);
            }
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
        return $this->readPackage($file);
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
     * @return Package
     *
     * @throws IOException
     */
    public function readPackage($source): Package
    {
        return Package::readPackage($source);
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
     */
    public function installFromArchive(string $zipFile): bool
    {
        $zip = new ZipFile($zipFile);
        /** @var Package $package */
        $package = null;

        if ($zip->has(Package::FILENAME)) {
            $zip->read(Package::FILENAME, function (array $stat, Stream $stream) use (&$package) {
                $package = $this->readPackage($stream);
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

    public function indexAll()
    {
        $modules = fs::scan($this->dir, ['excludeFiles' => true], 1);

        $gitIgnore = [];

        $name = function ($el) { return fs::name($el); };

        foreach ($modules as $module) {
            Console::log("Update Index of module ({0})", fs::name($module));

            $module = fs::name($module);

            $versions = fs::scan("$this->dir/$module", ['excludeFiles' => true], 1);

            foreach ($versions as $version) {
                $zipFile = "$version.zip";
                fs::delete($zipFile);

                $gitIgnore[] = "/$module/" . fs::name($version) . "/";

                $zip = new ZipFile($zipFile, true);
                $zip->addDirectory($version);
            }

            fs::formatAs(
                "$this->dir/$module/versions.json",
                flow($versions)->map($name)->toArray(),
                'json', JsonProcessor::SERIALIZE_PRETTY_PRINT
            );
        }

        fs::formatAs("$this->dir/modules.json", flow($modules)->map($name)->toArray(), 'json', JsonProcessor::SERIALIZE_PRETTY_PRINT);
        Stream::putContents("$this->dir/.gitignore", "/*/*/");
    }
}