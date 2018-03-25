<?php
namespace packager;

use php\io\IOException;
use php\io\Stream;
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
     * @return null|string
     */
    public function getMain(): ?string
    {
        return $this->data['main'];
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

    /**
     * @return array
     */
    public function getDevDeps(): array
    {
        return $this->data['devDeps'] ?: [];
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

    /**
     * @return array
     */
    public function getPlugins(): array
    {
        return $this->data['plugins'] ?: [];
    }

    /**
     * @param string $key
     * @param null $def
     * @return mixed|null
     */
    public function getAny(string $key, $def = null)
    {
        return $this->data[$key] ?? $def;
    }

    public function toString(): string
    {
        return $this->getName() . "@" . $this->getVersion('last');
    }


    /**
     * @param string|Stream $source
     * @return Package
     *
     * @throws IOException
     */
    static public function readPackage($source): Package
    {
        $stream = $source instanceof Stream ? $source : Stream::of($source, 'r');

        try {
            $data = $stream->parseAs('yaml');

            return new Package($data);
        } finally {
            $stream->close();
        }
    }
}