<?php
namespace php\util;

use Iterator;

/**
 * Class Flow
 * @package php\util
 */
class Flow implements Iterator {

    /**
     * @param array|Iterator $collection
     */
    public function __construct($collection) { }

    /**
     * Enables to save keys for the next operation
     * @return $this
     */
    public function withKeys() { return $this; }

    /**
     * @param array|Iterator $collection
     * @return Flow
     */
    public function append($collection) { return new Flow([]); }

    /**
     * @param callable $filter
     * @return Flow
     */
    public function find(callable $filter = null) { return new Flow([]); }

    /**
     * @param callable $filter
     * @return mixed
     */
    public function findOne(callable $filter = null) { return 0; }

    /**
     * @param callable $callback
     * @return Flow
     */
    public function group(callable $callback) { return new Flow([]); }

    /**
     * @param callable $callback ($el[, $key]): bool
     * @return int - iteration count
     */
    public function each(callable $callback) { return 0; }

    /**
     * @param $sliceSize
     * @param callable $callback (array $items): bool
     * @param bool $withKeys
     * @return int - slice iteration count
     */
    public function eachSlice($sliceSize, callable $callback, $withKeys = false) { return 0; }

    /**
     * @param callable $callback ($el[, $key])
     * @return Flow
     */
    public function map(callable $callback) { return new Flow([]); }

    /**
     * @param int $n - skip count
     * @return Flow
     */
    public function skip($n) { return new Flow([]); }

    /**
     * @param $count
     * @return Flow
     */
    public function limit($count) { return new Flow([]); }

    /**
     * @param callable $callback ($result, $el[, $key])
     * @return int
     */
    public function reduce(callable $callback) { return 0; }

    /**
     * @return array
     */
    public function toArray() { return []; }

    /**
     * @param string $separator
     * @return string
     */
    public function toString($separator) { return ''; }

    /**
     * @return int
     */
    public function count() { return 0; }

    /**
     * @return mixed
     */
    public function current() { return 0; }

    /**
     * @return void
     */
    public function next() {  }

    /**
     * @return mixed
     */
    public function key() { }

    /**
     * @return bool
     */
    public function valid() { return false; }

    /**
     * @return void
     */
    public function rewind() { }

    /**
     * @param array|Iterator $collection
     * @return Flow
     */
    public static function of($collection) { return new Flow([]); }

    /**
     * @param int $from
     * @param int $to
     * @param int $step
     * @return Flow
     */
    public static function ofRange($from, $to, $step = 1) { return new Flow([]); }

    /**
     * @param $string
     * @param int $chunkSize
     * @return Flow
     */
    public static function ofString($string, $chunkSize = 1) { return new Flow([]); }
}
