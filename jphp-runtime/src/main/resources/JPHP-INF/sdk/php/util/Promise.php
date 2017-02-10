<?php
namespace php\util;

/**
 * Class Promise
 * @package php\util
 * @packages std, core
 */
class Promise
{


    private function __construct() {}

    /**
     * @param mixed $initialValue (optional)
     * @param callable $callback ($value)  - assign callback
     * @return mixed
     */
    public static function create($initialValue, callable $callback) { }
}