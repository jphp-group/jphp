<?php
namespace php\io;

use Iterator;

abstract class Stream implements Iterator {
    /**
     * @var string
     */
    private $path;

    /**
     * @var string
     */
    private $mode;

    /**
     * @param int $length - count of bytes
     * @throws IOException
     * @return mixed
     */
    abstract public function read($length);

    /**
     * @throws IOException
     * @return mixed
     */
    abstract public function readFully();

    /**
     * @param string $value
     * @param null|int $length
     * @throws IOException
     * @return int
     */
    abstract public function write($value, $length = null);

    /**
     * @return bool
     */
    abstract public function eof();

    /**
     * @param int $position
     * @throws IOException
     * @return mixed
     */
    abstract public function seek($position);

    /**
     * @throws IOException
     * @return int
     */
    abstract public function getPosition();

    /**
     * @return mixed
     */
    abstract public function close();

    /**
     * @param string $path
     * @param null|string $mode
     * @return Stream
     */
    public function __construct($path, $mode = null){ }

    /**
     * @param $context
     * @return void
     */
    public function setContext($context) { }

    /**
     * @return mixed
     */
    public function getContext(){ }

    /**
     * @return mixed
     */
    public function current() { }

    /**
     * @return void
     */
    public function next() { }

    /**
     * @return mixed
     */
    public function key() { }

    /**
     * @return bool
     */
    public function valid() { }

    /**
     * @return void
     */
    public function rewind() { }


    /**
     * @param string $path
     * @param string $mode
     * @return Stream
     * @throws IOException
     */
    public static function of($path, $mode = 'r') { }

    /**
     * @param string $protocol
     * @param string $className
     */
    public static function register($protocol, $className) { }

    /**
     * @param $protocol
     */
    public static function unregister($protocol) { }
}
