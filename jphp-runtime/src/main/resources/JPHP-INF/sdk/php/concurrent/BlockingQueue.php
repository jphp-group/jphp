<?php
namespace php\concurrent;

use php\lang\InterruptedException;
use php\util\Collection;
use php\util\Queue;

/**
 * Class BlockingQueue
 * @package php\concurrent
 */
class BlockingQueue extends Queue
{
    /**
     * @param int $capacity
     * @param bool $fair
     */
    public function __construct($capacity, $fair = false) {}

    /**
     * @readonly
     * @var int
     */
    public $remainingCapacity;

    /**
     * Retrieves and removes the head of this queue, waiting if necessary
     * until an element becomes available.
     *
     * @return mixed
     */
    public function take() {}

    /**
     * Inserts the specified element into this queue, waiting if necessary
     * for space to become available.
     *
     * @param mixed $value
     */
    public function put($value) {}

    /**
     * Inserts the specified element into this queue, waiting up to the
     * specified wait time if necessary for space to become available.
     *
     * @param mixed $value
     * @param int $timeout (optional) in millis
     * @return bool
     *
     * @throws InterruptedException
     */
    public function offer($value, $timeout) {}

    /**
     * @param int $timeout in millis
     * @return mixed
     */
    public function poll($timeout) {}

    /**
     * Removes all available elements from this queue and adds them
     * to the given collection.  This operation may be more
     * efficient than repeatedly polling this queue.
     *
     * @param Collection $value
     * @param int $maxElements (optional)
     * @return int the number of elements transferred
     */
    public function drainTo(Collection $value, $maxElements) {}
}