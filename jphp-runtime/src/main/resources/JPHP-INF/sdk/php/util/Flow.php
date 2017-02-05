<?php
namespace php\util;

use Iterator;
use php\io\Stream;
use Traversable;

/**
 * A special class to work with arrays and iterators under flows.
 * Flows are used for the lazy array/iterator operations, to save the RAM memory.
 *
 * Class Flow, Immutable
 * @package php\util
 */
class Flow implements Iterator
{
    const __PACKAGE__ = 'std, core';

    /**
     * Create a new flow, you can also use ``of()`` method
     * @param array|Iterator $collection
     */
    public function __construct($collection) { }

    /**
     * Enables to save keys for the next operation
     * @return Flow
     */
    public function withKeys() { return $this; }

    /**
     * @param array|Traversable $keys
     * @param bool $ignoreCase
     * @return Flow
     */
    public function onlyKeys($keys, $ignoreCase = false) { return new Flow([]); }

    /**
     * Appends a new collection to the current flow,
     * do not remember that you can pass a flow to this method
     *
     * @param array|Iterator $collection
     * @return Flow
     */
    public function append($collection) { return new Flow([]); }

    /**
     * Finds elements by using the $filter callback,
     * elements - for each iteration that returns ``true``
     *
     * @param callable $filter
     * @return Flow
     */
    public function find(callable $filter = null) { return new Flow([]); }

    /**
     * Finds the first element by using the $filter callback,
     * when $filter will return the first ``true``
     *
     * @param callable $filter
     * @return mixed
     */
    public function findOne(callable $filter = null) { return 0; }

    /**
     * @param $value
     * @param bool $strict
     * @return int|null|string return null if not found, else - key of value
     */
    public function findValue($value, $strict = false) { return ''; }

    /**
     * @param callable $callback
     * @return Flow
     */
    public function group(callable $callback) { return new Flow([]); }

    /**
     * Iterates elements.
     * It will break if $callback returns ``false`` strongly
     *
     * @param callable $callback ($el[, $key]): bool
     * @return int - iteration count
     */
    public function each(callable $callback) { return 0; }

    /**
     * Iterates elements as slices (that are passing as arrays to $callback).
     * It will break if $callback returns ``false`` strongly
     *
     * @param int $sliceSize
     * @param callable $callback (array $items): bool
     * @param bool $withKeys
     * @return int - slice iteration count
     */
    public function eachSlice($sliceSize, callable $callback, $withKeys = false) { return 0; }

    /**
     * Iterates elements and returns a new flow of the result
     * Example::
     *
     *      $newFlow = Flow::of([1,2,3])->map(function($el){  return $el * 10 });
     *      // the new flow will contain 10, 20 and 30
     *
     * @param callable $callback ($el[, $key])
     * @return Flow
     */
    public function map(callable $callback) { return new Flow([]); }

    /**
     * Create a new flow by using the keys of the current flow
     * @return Flow
     */
    public function keys() { return new Flow([]); }

    /**
     * Skips $n elements in the current collection
     *
     * @param int $n skip count
     * @return Flow
     */
    public function skip($n) { return new Flow([]); }

    /**
     * Limits collection with $count
     *
     * @param int $count count of limit
     * @return Flow
     */
    public function limit($count) { return new Flow([]); }

    /**
     * Iterates elements and gets a result of this operation
     * It can be used for calculate some results, for example::
     *
     *  // calculates a sum of elements
     *  $sum = .. ->reduce(function($result, $el){  $result = $result + $el });
     *
     * @param callable $callback ($result, $el[, $key])
     * @return int
     */
    public function reduce(callable $callback) { return 0; }

    /**
     * Sort the last result of the flow, also see: ``php\\lib\\items::sort()``
     *
     * .. note:: use the ``withKeys()`` method to save keys
     *
     * @param callable $comparator ($o1, $o2) -> int, where -1 smaller, 0 equal, 1 greater
     * @return array
     */
    public function sort(callable $comparator = null) { return []; }

    /**
     * The same method as ``sort()`` only based on keys insteadof values
     *
     * .. note:: use the ``withKeys()`` method to save keys
     *
     * @param callable $comparator ($key1, $key2) -> int
     * @return array
     */
    public function sortByKeys(callable $comparator = null) { return []; }

    /**
     * Convert elements to an array
     *
     * .. note:: use the ``withKeys()`` method to save keys
     *
     * @return array
     */
    public function toArray() { return []; }

    /**
     * Join elements to a string similar to ``implode()`` in PHP
     *
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
     * Class is immutable, the disallowed clone method
     */
    private function __clone() { }

    /**
     * @return Flow
     */
    public static function ofEmpty() { return new Flow([]); }

    /**
     * Creates a new flow for an array of Iterator
     *
     * @param array|Traversable $collection
     * @return Flow
     */
    public static function of($collection) { return new Flow([]); }

    /**
     * Creates a new flow for a number range
     *
     * @param int $from
     * @param int $to
     * @param int $step
     * @return Flow
     */
    public static function ofRange($from, $to, $step = 1) { return new Flow([]); }

    /**
     * Creates a new flow for the string
     *
     * @param string $string
     * @param int $chunkSize how many characters to combine for one item ?
     * @return Flow
     */
    public static function ofString($string, $chunkSize = 1) { return new Flow([]); }

    /**
     * Creates a new flow for the Stream object
     *
     * @param Stream $stream stream object
     * @param int $chunkSize size for ``Stream.read($size)`` method
     * @return Flow
     */
    public static function ofStream(Stream $stream, $chunkSize = 1) { return new Flow([]); }
}
