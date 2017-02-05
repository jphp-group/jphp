<?php
namespace php\lang;

/**
 * Class System
 * @package php\lang
 */
final class System
{
    const __PACKAGE__ = 'std, core';

    private function __construct() { }

    /**
     * Exit from program with status globally
     * @param int $status
     */
    public static function halt($status) { }

    /**
     * Runs the garbage collector
     */
    public static function gc() { }

    /**
     * @return string[]
     */
    public static function getEnv() { }

    /**
     * Gets a system property by name
     *
     * @param $name
     * @param string $def
     * @return string
     */
    public static function getProperty($name, $def = '') { return ''; }

    /**
     * Sets a system property by name and value.
     * @param string $name
     * @param string $value
     * @return string
     */
    public static function setProperty($name, $value)
    {
    }

    /**
     * @return array
     */
    public static function getProperties()
    {
    }

    /**
     * @param array $properties
     */
    public static function setProperties(array $properties)
    {
    }
}
