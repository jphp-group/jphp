<?php
namespace packager\repository;

/**
 * Class ExternalRepository
 * @package packager\repository
 */
abstract class ExternalRepository
{
    /**
     * @var string
     */
    private $source;

    public function __construct(string $source)
    {
        $this->source = $source;
    }

    /**
     * @return string
     */
    public function getSource(): string
    {
        return $this->source;
    }

    abstract public function downloadTo(string $pkgName, string $pkgVersion, string $toFile): bool;
    abstract public function getVersions(string $pkgName): array;
}