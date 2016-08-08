<?php
namespace php\compress;

/**
 * Class ZipEntry
 * @package php\compress
 */
class ZipEntry
{
    /**
     * @readonly
     * @var string
     */
    public $name;

    /**
     * @var string
     */
    public $comment;

    /**
     * @var int
     */
    public $size;

    /**
     * @var int
     */
    public $compressedSize;

    /**
     * @var int
     */
    public $time;

    /**
     * @var int
     */
    public $crc;

    /**
     * @var int
     */
    public $method;

    /**
     * @var string binary
     */
    public $extra;

    /**
     * ZipEntry constructor.
     * @param string $name
     */
    public function __construct($name)
    {
    }

    /**
     * @return bool
     */
    public function isDirectory()
    {
    }
}