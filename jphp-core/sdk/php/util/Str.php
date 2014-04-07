<?php
namespace php;

final class Str {

    private function __construct() {}

    /**
     * @param string $string
     * @param string $search
     * @return int
     */
    public static function pos($string, $search) { return 0; }

    /**
     * @param $string
     * @param $search
     * @return int
     */
    public static function lastPos($string, $search) { return 0; }

    /**
     * @param string $string
     * @param int $beginIndex
     * @param null|int $endIndex
     * @return string - return false if params are invalid
     */
    public static function sub($string, $beginIndex, $endIndex = null) { return ''; }

    /**
     * @param string $string
     * @param string $pattern
     * @return bool
     */
    public static function match($string, $pattern) { return false; }
}
