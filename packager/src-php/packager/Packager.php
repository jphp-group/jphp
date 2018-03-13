<?php
namespace packager;

use php\io\IOException;
use php\io\Stream;
use php\lib\fs;

/**
 * Class Packager
 * @package packager
 */
class Packager
{
    /**
     * @param string|Stream $source
     * @return Package
     *
     * @throws IOException
     */
    public function readPackage($source): Package
    {
        $stream = $source instanceof Stream ? $source : Stream::of($source, 'r');

        try {
            $data = $stream->parseAs('yaml');

            return new Package($data);
        } finally {
            $stream->close();
        }
    }

    /**
     * @param Package $package
     * @param string $directory
     */
    public function writePackage(Package $package, string $directory)
    {
        fs::formatAs($directory . "/" . Package::FILENAME, $package->toArray(), 'yaml');
    }
}