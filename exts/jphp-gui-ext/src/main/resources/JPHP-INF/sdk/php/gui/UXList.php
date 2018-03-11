<?php
namespace php\gui;

use ArrayAccess;
use Countable;
use Iterator;

/**
 * Class UXList
 * @package php\gui
 *
 *
 * @packages gui, javafx
 */
class UXList implements Iterator, Countable, ArrayAccess
{
    /**
     * @readonly
     * @var int
     */
    public $count = 0;

    /**
     * @return bool
     */
    public function isEmpty(): bool
    {
    }

    /**
     * @return bool
     */
    public function isNotEmpty(): bool
    {
    }

    /**
     * @param $object
     * @return int -1 if not found
     */
    public function indexOf($object): int
    {
    }

    /**
     * @param $object
     * @return bool
     */
    public function has($object): bool
    {
    }

    /**
     * @param mixed $object
     */
    public function add($object)
    {
    }

    /**
     * @param int $index
     * @param mixed $object
     */
    public function insert(int $index, $object)
    {
    }

    /**
     * @param mixed $object
     * @param mixed $newObject
     */
    public function replace($object, $newObject)
    {
    }

    /**
     * @param iterable $objects
     */
    public function addAll(iterable $objects)
    {
    }

    /**
     * @param int $index
     * @param $object
     */
    public function set(int $index, $object)
    {
    }

    /**
     * @param iterable $objects
     */
    public function setAll(iterable $objects)
    {
    }

    /**
     * @param int $index
     * @param iterable $objects
     */
    public function insertAll(int $index, iterable $objects)
    {
    }

    /**
     * @param mixed $object
     */
    public function remove($object)
    {
    }

    /**
     * @param int $index
     */
    public function removeByIndex(int $index)
    {
    }

    /**
     * ...
     */
    public function clear()
    {
    }

    /**
     * @return mixed|null null if not found
     */
    public function last()
    {
    }

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

    /**
     * {@inheritdoc}
     */
    public function count()
    {
    }

    /**
     * {@inheritdoc}
     */
    public function offsetExists($offset)
    {

    }

    /**
     * {@inheritdoc}
     */
    public function offsetGet($offset)
    {

    }

    /**
     * {@inheritdoc}
     */
    public function offsetSet($offset, $value)
    {
        if ($offset != null) {
            throw new \Exception("Unable to modify the list");
        }
    }

    /**
     * {@inheritdoc}
     */
    public function offsetUnset($offset)
    {
    }

    /**
     * @param callable $callback
     */
    public function addListener(callable $callback)
    {
    }

    /**
     * @return array
     */
    public function toArray(): array
    {
    }

    /**
     * Available to clone.
     */
    public function __clone()
    {
    }
}