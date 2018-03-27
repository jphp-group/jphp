<?php
namespace packager\repository;

use php\lib\fs;

class ServerRepository extends ExternalRepository
{
    public function downloadTo(string $pkgName, string $pkgVersion, string $toFile): bool
    {
        $copied = fs::copy(
            "{$this->getSource()}/repo/$pkgName?version=$pkgVersion&download=1", $toFile
        );

        return $copied > -1;
    }

    public function getVersions(string $pkgName): array
    {
        return fs::parseAs("{$this->getSource()}/repo/$pkgName/find", "json");
    }
}