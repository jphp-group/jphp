<?php
namespace php\lang;
use php\io\Stream;

/**
 * Class System
 * @packages std, core
 */
final class System
{


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

    /**
     * @return Stream
     */
    public static function in(): Stream
    {
    }

    /**
     * @return Stream
     */
    public static function out(): Stream
    {
    }

    /**
     * @return Stream
     */
    public static function err(): Stream
    {
    }

    /**
     * Set stdin stream.
     * @param null|Stream $in
     */
    public static function setIn(?Stream $in)
    {
    }

    /**
     * Set stdout stream.
     * @param null|Stream $out
     * @param string|null $encoding
     */
    public static function setOut(?Stream $out, string $encoding = null)
    {
    }

    /**
     * Set stderr stream.
     * @param null|Stream $err
     * @param string|null $encoding
     */
    public static function setErr(?Stream $err, string $encoding = null)
    {
    }

    /**
     * @return string
     */
    public static function tempDirectory(): string
    {
    }

    /**
     * @return string
     */
    public static function userDirectory(): string
    {
    }

    /**
     * @return string
     */
    public static function userHome(): string
    {
    }

    /**
     * @return string
     */
    public static function userName(): string
    {
    }

    /**
     * @return string
     */
    public static function osName(): string
    {
    }

    /**
     * @return string
     */
    public static function osVersion(): string
    {
    }
}
