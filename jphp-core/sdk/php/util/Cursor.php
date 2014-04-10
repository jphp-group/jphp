<?php
namespace php\util;

use Iterator;

/**
 * Class Cursor
 * @package php\util
 */
class Cursor implements Iterator {

    private function __construct(){}

    /**
     * @param array|\Iterator $collection
     * @return Cursor
     */
    public function append($collection) { return new Cursor(); }

    /**
     * @param callable $filter
     * @return Cursor
     */
    public function find(callable $filter = null) { return new Cursor(); }

    /**
     * @param callable $callback ($el[, $key]): bool
     * @return int - iteration count
     */
    public function each(callable $callback) { return 0; }

    /**
     * @param $sliceSize
     * @param callable $callback (array $items): bool
     * @return int - slice iteration count
     */
    public function eachSlice($sliceSize, callable $callback) { return 0; }

    /**
     * @param callable $callback ($el[, $key])
     * @return Cursor
     */
    public function map(callable $callback) { return new Cursor(); }

    /**
     * @param int $n - skip count
     * @return Cursor
     */
    public function skip($n) { return new Cursor(); }

    /**
     * @param $count
     * @return Cursor
     */
    public function limit($count) { return new Cursor(); }

    /**
     * @param callable $callback ($result, $el[, $key])
     * @return int
     */
    public function reduce(callable $callback) { return 0; }

    /**
     * @param bool $withKeys
     * @return array
     */
    public function toArray($withKeys = false) { return []; }

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

    public function next() { }

    public function key() { }

    public function valid() { }

    public function rewind() { }

    /**
     * @param array|Iterator $collection
     * @return Cursor
     */
    public static function of($collection) { return new Cursor(); }

    /**
     * @param int $from
     * @param int $to
     * @param int $step
     * @return Cursor
     */
    public static function ofRange($from, $to, $step = 1) { return new Cursor(); }

    /**
     * @param $string
     * @param int $chunkSize
     * @return Cursor
     */
    public static function ofString($string, $chunkSize = 1) { return new Cursor(); }
}
