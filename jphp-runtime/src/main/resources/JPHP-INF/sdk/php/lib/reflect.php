<?php
namespace php\lib;
use php\lang\Module;

/**
 * Class reflect
 * @package php\lib
 */
class reflect
{
    const __PACKAGE__ = 'std, core';

    private function __construct()
    {
    }

    /**
     * @param object $object
     * @param bool $isLowerCase (optional)
     * @return false|string
     */
    public static function typeOf($object, $isLowerCase = false)
    {
    }

    /**
     * @param string $typeName
     * @return Module|null
     */
    public static function typeModule($typeName)
    {
    }

    /**
     * @param string $funcName
     * @return Module|null
     */
    public static function functionModule($funcName)
    {
    }

    /**
     * @param string $className
     * @param array $args
     * @param bool $withConstruct
     * @return object
     */
    public static function newInstance($className, array $args = null, $withConstruct = true)
    {
    }
}