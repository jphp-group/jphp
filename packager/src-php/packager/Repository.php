<?php
namespace packager;
use php\compress\ZipFile;
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
     * Repository constructor.
     * @param string $directory
     */
    public function __construct(string $directory)
    {
        $this->dir = $directory;
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
     * @param string $versionPattern
     * @return null|Package
     */
    public function findPackage(string $name, string $versionPattern): ?Package
    {
        $versions = $this->getPackageVersions($name);
        arr::sort($versions);
        $versions = arr::reverse($versions);

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
        $stream = $source instanceof Stream ? $source : Stream::of($source, 'r');

        try {
            $data = $stream->parseAs('yaml');

            return new Package($data);
        } finally {
            $stream->close();
        }
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
    public function installFromArchive(string $zipFile)
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
        }
    }
}