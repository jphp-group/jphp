<?php
namespace packager;

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
     * @return null|string
     */
    public function getVersion(): ?string
    {
        return $this->data['version'];
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
}