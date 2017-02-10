<?php
namespace php\io;

/**
 * Class MiscStream
 * @package php\io
 * @packages std, core
 */
class MiscStream extends Stream
{


    /**
     * @param int $length - count of bytes
     * @throws IOException
     * @return mixed
     */
    public function read($length) { }

    /**
     * @throws IOException
     * @return mixed
     */
    public function readFully() { }

    /**
     * @param string $value
     * @param null|int $length
     * @throws IOException
     * @return int
     */
    public function write($value, $length = null) { }

    /**
     * @return bool
     */
    public function eof() { }

    /**
     * @param int $position
     * @throws IOException
     * @return mixed
     */
    public function seek($position) { }

    /**
     * @throws IOException
     * @return int
     */
    public function getPosition() { }

    /**
     * @return mixed
     */
    public function close() { }

    /**
     * @return int
     */
    public function length() { }

    /**
     * @throws IOException
     */
    public function flush() { }
}
