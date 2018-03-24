<?php
namespace packager;


use php\io\File;

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
}