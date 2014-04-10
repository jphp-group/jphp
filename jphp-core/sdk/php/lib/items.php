<?php
namespace php\lib;

/**
 * Library for working with enumerations - arrays, iterators, etc.
 *
 * Class items
 * @package php\lib
 */
class items {

    private function __construct() { }

    /**
     * @param array|\Countable|\Iterator $collection
     *      - warning: for iterators it will iterate all elements to return the result
     * @return int
     */
    public static function count($collection) { return 0; }

    /**
     * @param array|\Iterator $collection
     * @param callable $callback -> ($value, $key): bool
     *          to break iteration, return *FALSE* (strongly) from $callback
     * @return int - iteration count
     */
    public static function each($collection, callable $callback) { return 0; }

    /**
     * @param array|\Iterator $collection
     * @param int $size - slice size
     * @param callable $callback -> (array $slice): bool
     *          to break iteration, return *FALSE* (strongly) from $callback
     * @return int - iteration slice count
     */
    public static function eachSlice($collection, $size, callable $callback) { return 0; }

    /**
     * Finds the first value matching the $callback condition
     *
     * @param array|\Iterator $collection
     * @param callable|null $callback -> ($value, $key): bool
     * @return mixed|null - null if not found
     */
    public static function find($collection, callable $callback = null) { return []; }

    /**
     * Finds the all values matching the $callback condition. Keys are preserved.
     *
     * @param array|\Iterator $collection
     * @param callable $callback
     * @return array
     */
    public static function findAll($collection, callable $callback = null) { return []; }

    /**
     * @param array|\Iterator $collection
     * @param int $count
     * @return array|mixed
     *          - returns non-array (one element) if passed $count <= 1
     */
    public static function first($collection, $count = 1) { return []; }

    /**
     * @param array|\Iterator $collection
     * @param callable $callback -> ($value, $key): mixed
     * @return array
     */
    public static function map($collection, callable $callback) { return []; }

    /**
     * Combines all elements of enum by applying a binary operation, specified by a callback
     *
     * @param array|\Iterator $collection
     * @param callable $callback
     * @return mixed|null
     */
    public static function reduce($collection, callable $callback) { }

    /**
     * @param array|\Iterator $collection
     * @param bool $withKeys
     * @return array
     */
    public static function toArray($collection, $withKeys = false) { return []; }

    /**
     * Returns all keys of collection
     *
     * @param array|\Iterator $collection
     * @return array
     */
    public static function keys($collection) { return []; }

    /**
     * Returns a new array that is a one-dimensional flattening of this collection (recursively).
     * That is, for every element that is an collection, extract its elements into the new array.
     * If the optional $maxLevel argument > -1 the level of recursion to flatten.
     *
     * @param array|\Iterator $collection
     * @param int $maxLevel
     * @return array
     */
    public static function flatten($collection, $maxLevel = -1) { return []; }
}
