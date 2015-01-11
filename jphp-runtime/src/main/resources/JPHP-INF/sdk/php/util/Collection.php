<?php
namespace php\util;

use Countable;
use Iterator;
use Traversable;

/**
 * Class Collection
 * @package php\util
 */
abstract class Collection implements Countable, Iterator
{
    /**
     * @readonly
     * @var bool
     */
    public $empty;

    /**
     * @param mixed $value
     */
    public function add($value) {}

    /**
     * @param Traversable|array $value
     */
    public function addAll($value) {}

    /**
     * @param mixed $value
     */
    public function remove($value) {}

    /**
     * Remove all elements.
     */
    public function clear() {}

    /**
     * {@inheritdoc}
     * @return int
     */
    public function count() {}

    /**
     * Getter of empty property
     * @return bool
     */
    protected function isEmpty() {}

    /**
     * {@inheritdoc}
     */
    public function current()
    {
    }

    /**
     * {@inheritdoc}
     */
    public function next()
    {
    }

    /**
     * {@inheritdoc}
     */
    public function key()
    {
    }

    /**
     * {@inheritdoc}
     */
    public function valid()
    {
    }

    /**
     * {@inheritdoc}
     */
    public function rewind()
    {
    }
}