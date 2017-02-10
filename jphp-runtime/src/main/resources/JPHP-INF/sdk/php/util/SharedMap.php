<?php
namespace php\util;

use Countable;
use Iterator;
use Traversable;

/**
 * Class SharedMap
 * @package php\util
 * @packages std, core
 */
class SharedMap extends SharedCollection
{


    /**
     * @param array|Traversable $array (optional)
     */
    public function __construct($array)
    {
    }

    /**
     * @param string $key
     * @param mixed $default
     * @return mixed
     */
    public function get($key, $default = null)
    {
    }

    /**
     * @param string $key
     * @param callable $createCallback
     * @return mixed
     */
    public function getOrCreate($key, callable $createCallback)
    {
    }

    /**
     * @param string $key
     * @return bool
     */
    public function has($key)
    {
    }

    /**
     * @return int
     */
    public function count()
    {
    }

    /**
     * @param string $key
     * @param mixed $value
     * @param bool $override
     * @return mixed previous value
     */
    public function set($key, $value, $override = true)
    {
    }

    /**
     * @param string $key
     * @return mixed
     */
    public function remove($key)
    {
    }

    /**
     * Remove all items.
     */
    public function clear()
    {
    }

    /**
     * @return bool
     */
    public function isEmpty()
    {
    }
}