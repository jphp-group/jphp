<?php
namespace packager;


use php\io\File;
use php\io\IOException;
use php\lib\fs;
use semver\SemVersion;

class Vendor
{
    /**
     * @var string
     */
    private $dir;

    /**
     * Vendor constructor.
     * @param string $dir
     */
    public function __construct(string $dir)
    {
        $this->dir = $dir;
    }

    /**
     * @return string
     */
    public function getDir(): string
    {
        return $this->dir;
    }

    /**
     * @param Package $package
     * @param string $path
     * @return File
     */
    public function getFile(Package $package, string $path): File
    {
        return new File("$this->dir/{$package->getName()}/$path");
    }

    /**
     * @param Package $package
     * @param string $path
     * @return string
     */
    public function getRelativeFile(Package $package, string $path): string
    {
        return "{$package->getName()}/$path";
    }

    /**
     * @param string $name
     * @return Package
     */
    public function getPackage(string $name): ?Package
    {
        $file = new File("$this->dir/{$name}/", Package::FILENAME);

        if (!$file->isFile()) {
            return null;
        }

        $installedPackage = Package::readPackage($file);
        return $installedPackage;
    }

    /**
     * @param Package $package
     * @param null|PackageLock $lock
     * @return bool
     */
    public function alreadyInstalled(Package $package, ?PackageLock $lock): bool
    {
        try {
            $file = $this->getFile($package, Package::FILENAME);

            if (!$file->isFile()) {
                return false;
            }

            $installedPackage = Package::readPackage($file, $lock->findVersionInfo($package->getName()));

            if (!$installedPackage->isIdentical($package)) {
                return false;
            }

            if ($package->getVersion() === $installedPackage->getVersion()) {
                return true;
            }

            return false;
        } catch (IOException $e) {
            return false;
        }
    }

    public function clean()
    {
        fs::clean($this->getDir());
    }

    public function fetchPaths(): array
    {
        if (fs::isFile("{$this->getDir()}/paths.json")) {
            return (array)fs::parseAs("{$this->getDir()}/paths.json", 'json');
        } else {
            return [];
        }
    }
}