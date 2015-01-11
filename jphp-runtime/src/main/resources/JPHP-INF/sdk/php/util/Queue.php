<?php
namespace php\util;

/**
 * Class Queue
 * @package php\util
 */
abstract class Queue extends Collection
{
    /**
     * @param mixed $value (optional)
     * @return mixed
     */
    public function remove($value) {}

    /**
     * @return mixed|null null if queue is empty
     */
    public function peek() {}

    /**
     * Removes last element and returns it.
     * @return mixed|null null if queue is empty
     */
    public function poll() {}

    /**
     * Inserts the specified element into this queue
     * if it is possible to do so immediately without violating capacity restrictions.
     * When using a capacity-restricted queue, this method is generally preferable to add(E),
     * which can fail to insert an element only by throwing an exception.
     *
     * @param mixed $value
     * @return bool if the element was added to this queue, else false
     */
    public function offer($value) {}
}