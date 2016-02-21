<?php
namespace php\lib;

/**
 * Class reflect
 * @package php\lib
 */
class reflect
{
    private function __construct() {}

    /**
     * @param object $object
     * @param bool $isLowerCase (optional)
     * @return false|string
     */
    public static function typeOf($object, $isLowerCase = false) {}

    /**
     * @param string $className
     * @param array $args
     * @param bool $withConstruct
     * @return object
     */
    public static function newInstance($className, array $args = null, $withConstruct = true) {}
}