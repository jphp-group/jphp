<?php
namespace php\io;

abstract class Stream {
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
     * @throws IOException
     */
    public function __construct($path, $mode = null){ }

    /**
     * @param $context
     */
    public function setContext($context) { }

    /**
     * @return mixed
     */
    public function getContext(){ }


    /**
     * @param string $path
     * @param string $mode
     * @return Stream
     * @throws IOException
     */
    public static function create($path, $mode = 'r') { }

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