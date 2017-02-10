<?php
namespace php\util;

/**
 * Class SharedValue
 * @package php\util
 * @packages std, core
 */
class SharedValue extends SharedMemory
{


    /**
     * @param mixed $value (optional)
     */
    public function __construct($value)
    {
    }

    /**
     * @return mixed
     */
    public function get()
    {
    }

    /**
     * @param mixed $value
     * @param bool $override
     * @return mixed
     */
    public function set($value, $override = true)
    {
    }

    /**
     * @return mixed
     */
    public function remove()
    {
    }

    /**
     * @return bool
     */
    public function isEmpty()
    {
    }

    /**
     * @param callable $updateCallback ($oldValue) returns a new value
     * @return mixed
     */
    public function getAndSet(callable $updateCallback)
    {
    }

    /**
     * @param callable $updateCallback ($oldValue) returns a new value
     * @return mixed
     */
    public function setAndGet(callable $updateCallback)
    {
    }
}
