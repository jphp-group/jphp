<?php
namespace j\java\lang;

use php\io\Stream;

/**
 * Class System
 * @package j\java\lang
 */
class System
{
    /**
     * @var Stream
     */
    static $out;

    /**
     * @var Stream
     */
    static $err;

    /**
     * @var Stream
     */
    static $in;

    /**
     * @param $name (optional)
     * @return array|string
     */
    public static function getenv($name)
    {
    }

    /**
     * @return array
     */
    public static function getProperties()
    {
    }

    /**
     * @param $value
     * @return mixed
     */
    public static function getProperty($value)
    {
    }

    /**
     * @return int
     */
    public static function currentTimeMillis() {
    }

    /**
     * @return int
     */
    public static function nanoTime() {
    }

    /**
     * @param $object
     * @return int
     */
    public static function identityHashCode($object)
    {
    }

    /**
     * @return string
     */
    public static function lineSeparator()
    {
    }
}