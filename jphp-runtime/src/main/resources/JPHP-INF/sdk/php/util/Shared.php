<?php
namespace php\util;

/**
 * Class to work with shared memory of Environments
 *
 * Class Shared
 * @packages std, core
 * @package php\util
 */
class Shared
{


    private function __construct()
    {
    }

    /**
     * Get or create if does not exist and get a shared value
     * @param string $name
     * @param callable $creator returns init value
     * @return SharedValue
     */
    public static function value($name, callable $creator = null)
    {
    }

    /**
     * Removes the value by $name.
     * @param String $name
     * @return SharedValue removed value
     */
    public static function reset($name)
    {
    }

    /**
     * Removes the all shared memory values.
     */
    public static function resetAll()
    {
    }
}
