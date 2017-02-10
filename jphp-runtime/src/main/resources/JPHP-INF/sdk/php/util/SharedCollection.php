<?php
namespace php\util;

use Countable;
use Traversable;

/**
 * Class SharedCollection
 * @package php\util
 * @packages std, core
 */
abstract class SharedCollection extends SharedMemory implements Countable, Traversable
{


    /**
     * @return bool
     */
    abstract public function isEmpty();

    /**
     * @return int
     */
    abstract public function count();

    /**
     * Remove all elements.
     * @return void
     */
    abstract public function clear();
}