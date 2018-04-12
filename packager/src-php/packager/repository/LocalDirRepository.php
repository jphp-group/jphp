<?php
namespace packager\repository;

use compress\GzipOutputStream;
use compress\TarArchive;
use packager\Repository;
use php\lib\fs;
use php\lib\str;

/**
 * Class LocalDirRepository
 * @package packager\repository
 */
class LocalDirRepository extends ExternalRepository
{
    /**
     * @return bool
     */
    public function isFit(): bool
    {
        return str::startsWith($this->getSource(), 'file://');
    }

    /**
     * @param string $pkgName
     * @param string $pkgVersion
     * @param string $toFile
     * @return bool
     */
    public function downloadTo(string $pkgName, string $pkgVersion, string $toFile): bool
    {
        $arch = new TarArchive(new GzipOutputStream($toFile));
        $arch->open();

        $dir = "{$this->getSource()}/$pkgName/$pkgVersion/";

        fs::scan($dir, function ($filename) use ($arch, $dir) {
            $arch->addFile($filename, fs::relativize($filename, $dir));
        });

        $arch->close();
        return true;
    }

    /**
     * @param string $pkgName
     * @return array
     */
    public function getVersions(string $pkgName): array
    {
        $dir = "{$this->getSource()}/$pkgName/";

        $dirs = fs::scan($dir, ['excludeFiles' => true], 1);

        $result = [];

        foreach ($dirs as $version) {
            $result[$version] = Repository::calcPackageInfo($version);
        }

        return $result;
    }
}