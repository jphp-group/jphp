<?php
namespace compress;

use php\io\File;
use php\io\IOException;
use php\io\Stream;

/**
 * @package compress
 * @packages compress
 */
class GzipInputStream extends Stream
{
    /**
     * GzipOutStream constructor.
     * @param Stream|File|string $input
     * @param bool $decompressConcatenated
     */
    public function __construct($input, bool $decompressConcatenated = false)
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