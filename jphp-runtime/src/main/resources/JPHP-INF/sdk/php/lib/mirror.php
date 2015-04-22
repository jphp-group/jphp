<?php
namespace php\lib;


/**
 * Reflection Lib.
 *
 * Class mirror
 * @package php\lib
 */
class Mirror {
    private function __construct() {}

    /**
     * @param object $object
     * @param bool $toLowerCase (optional)
     * @return false|string
     */
    public static function typeOf($object, $toLowerCase = false) {}

    /**
     * @param string $className
     * @param array $args
     * @param bool $withConstruct
     * @return object
     */
    public static function newInstance($className, array $args = null, $withConstruct = true) {}
}