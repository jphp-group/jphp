<?php
namespace packager\repository;

use compress\GzipOutputStream;
use compress\TarArchive;
use packager\cli\Console;
use packager\Ignore;
use packager\Package;
use packager\Packager;
use packager\Repository;
use php\io\IOException;
use php\lib\fs;
use php\lib\str;

/**
 * Class LocalDirRepository
 * @package packager\repository
 */
class LocalDirRepository extends SingleExternalRepository
{
    /**
     * @return bool
     */
    public function isFit(): bool
    {
        return fs::isDir($this->getSource())
            || str::startsWith($this->getSource(), 'file:');
    }

    /**
     * @return string
     */
    public function getNormalSource(): string
    {
        $source = $this->getSource();

        if (str::startsWith($source, 'file:')) {
            $source = str::sub($source, 5);
        }

        return fs::abs($source);
    }

    /**
     * @param string $pkgName
     * @param string $pkgVersion
     * @param string $toFile
     * @return bool
     */
    public function downloadTo(string $pkgName, string $pkgVersion, string $toFile): bool
    {
        return false;
    }

    /**
     * @param string $pkgName
     * @param string $pkgVersion
     * @param string $toDir
     * @return bool
     */
    public function downloadToDirectory(string $pkgName, string $pkgVersion, string $toDir): bool
    {
        $dir = $this->getNormalSource();

        try {
            $realPkg = Package::readPackage("$dir/" . Package::FILENAME);
        } catch (IOException $e) {
            Console::error("Package '{0}' hasn't 'package.php.yml' file.", $pkgName);
            return false;
        }

        if ($realPkg->getName() !== $pkgName) {
            Console::error("Invalid package name '{0}', real name is '{1}'", $pkgName, $realPkg->getName());
            return false;
        }

        fs::clean($toDir);

        $ignore = $realPkg->fetchIgnore();

        fs::scan($dir, function ($filename) use ($dir, $toDir, $ignore) {
            $name = fs::relativize($filename, $dir);

            if (fs::isFile($filename) && !$ignore->test($name) && !str::startsWith($name, '.git/')) {
                fs::ensureParent("$toDir/$name");
                fs::copy($filename, "$toDir/$name", null, 1024 * 256);
            }
        });

        return true;
    }

    /**
     * @param string $pkgName
     * @return array
     */
    public function getVersions(string $pkgName): array
    {
        try {
            $source = $this->getNormalSource();

            $info = Repository::calcPackageInfo(
                $source, 'sha-1'
            );

            return [
                $this->getHash() => [
                    'realVersion' => $this->getHash(),
                    'hash' => $info['sha1'],
                    'path' => $source
                ]
            ];
        } catch (IOException $e) {
            Console::error("Package '{0}' hasn't 'package.php.yml' file.", $pkgName);
            return [];
        }
    }

    public function getHash(): string
    {
        return str::hash($this->getNormalSource());
    }

    public function getCacheTime(): string
    {
        return '5m';
    }
}