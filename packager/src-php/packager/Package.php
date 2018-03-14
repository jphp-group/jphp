<?php
namespace packager;
use php\lib\arr;

/**
 * Class Package
 * @package packager
 */
class Package
{
    const FILENAME = "package.php.yml";

    /**
     * @var array
     */
    private $data = [];

    /**
     * Package constructor.
     * @param array $data
     */
    public function __construct(array $data)
    {
        $this->data = $data;
    }

    /**
     * @return string
     */
    public function getName(): string
    {
        return $this->data['name'];
    }

    /**
     * @param null|string $def
     * @return null|string
     */
    public function getVersion(string $def = null): ?string
    {
        return $this->data['version'] ?: $def;
    }

    /**
     * @return array
     */
    public function getJars(): array
    {
        return $this->data['jars'] ?: [];
    }

    /**
     * @return array
     */
    public function getDeps(): array
    {
        return $this->data['deps'] ?: [];
    }

    public function toArray(): array
    {
        return $this->data;
    }

    public function getDescription(): ?string
    {
        return $this->data['description'] ?: null;
    }

    /**
     * @return array
     */
    public function getSources(): array
    {
        return $this->data['sources'] ?: [];
    }
}