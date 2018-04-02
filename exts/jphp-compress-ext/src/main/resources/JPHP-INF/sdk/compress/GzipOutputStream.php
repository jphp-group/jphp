<?php
namespace compress;

use php\io\File;
use php\io\IOException;
use php\io\Stream;

/**
 * Class GzipOutStream
 * @package compress
 * @packages compress
 */
class GzipOutputStream extends Stream
{
    /**
     * GzipOutStream constructor.
     * @param Stream|File|string $output
     * @param array $parameters [comment, filename, compressionLevel, modificationTime, operatingSystem]
     */
    public function __construct($output, array $parameters = null)
    {
    }

    /**
     * @param int $length - count of bytes
     * @throws IOException
     * @return mixed
     */
    public function read($length)
    {
    }

    /**
     * @throws IOException
     * @return mixed
     */
    public function readFully()
    {
    }

    /**
     * @param string $value
     * @param null|int $length
     * @throws IOException
     * @return int
     */
    public function write($value, $length = null)
    {
    }

    /**
     * @return bool
     */
    public function eof()
    {
        return false;
    }

    /**
     * @param int $position
     * @throws IOException
     * @return mixed
     */
    public function seek($position)
    {
    }

    /**
     * @throws IOException
     * @return int
     */
    public function getPosition()
    {
    }

    /**
     * @return mixed
     */
    public function close()
    {
    }
}