<?php
namespace php\lib;

/**
 * Library for working with collections - arrays, iterators, etc.
 *
 * Class item
 * @package php\lib
 */
final class item {

    private function __construct() { }

    /**
     * @param array|\Iterator $traversable
     * @param callable $callback
     */
    public function each($traversable, callable $callback) { }
}
