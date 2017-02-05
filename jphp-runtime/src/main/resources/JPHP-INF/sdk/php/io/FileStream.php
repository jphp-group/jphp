<?php
namespace php\io;


class FileStream extends Stream
{
    const __PACKAGE__ = 'std, core';

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
     * @return int
     * @throws IOException
     */
    public function getFilePointer() { }


    /**
     * @param int $size
     * @throws IOException
     */
    public function truncate($size) { }
}