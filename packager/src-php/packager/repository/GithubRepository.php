<?php
namespace packager\repository;


use php\lib\fs;
use php\net\URL;

class GithubRepository extends ExternalRepository
{
    /**
     * @var URL
     */
    private $sourceUrl;

    public function __construct($source)
    {
        parent::__construct($source);

        $this->sourceUrl = new URL($source);
    }

    public function getVersions(string $pkgName): array
    {
        $versions = fs::parseAs(
            "https://raw.githubusercontent.com{$this->sourceUrl->getPath()}/master/$pkgName/versions.json",
            "json"
        );

        return $versions;
    }

    public function downloadTo(string $pkgName, string $pkgVersion, string $toFile): bool
    {
        $copied = fs::copy(
            "{$this->getSource()}/raw/master/$pkgName/$pkgVersion.zip", $toFile
        );

        return $copied > 0;
    }
}