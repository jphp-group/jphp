<?php
namespace php\lib;

/**
 * Class rx
 * @package php\lib
 */
class rx
{
    /**
     * @param mixed $value
     * @return mixed
     */
    public static function observable($value = null)
    {
    }

    /**
     * @param mixed $observable
     * @param callable $callback
     */
    public static function subscribe($observable, callable $callback)
    {
    }

    /**
     * @param mixed $observable
     * @param callable $callback
     */
    public static function unsubscribe($observable, callable $callback)
    {
    }

    /**
     * @param mixed $observable
     * @return callable[]
     */
    public static function subscribers($observable)
    {
    }

    /**
     * @param mixed $observable
     */
    public static function unsubscribeAll($observable)
    {
    }
}