<?php
namespace php\lang;

/**
 * Class System
 * @package php\lang
 */
final class System {

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
     * Gets a system property by name
     *
     * @param $name
     * @param string $def
     * @return string
     */
    public static function getProperty($name, $def = '') { return ''; }
}
