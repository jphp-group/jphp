<?php
namespace packager\repository;


use php\lang\IllegalArgumentException;
use php\lib\fs;
use php\lib\str;
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

        try {
            $this->sourceUrl = new URL($source);
        } catch (IllegalArgumentException $e) {
            // ...
        }
    }

    public function isFit(): bool
    {
        return str::startsWith($this->getSource(), 'https://github.com/')
            || str::startsWith($this->getSource(), 'http://github.com/');
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
            "{$this->getSource()}/raw/master/$pkgName/$pkgVersion.tar.gz", $toFile
        );

        return $copied > 0;
    }
}