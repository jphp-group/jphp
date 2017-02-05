<?php
namespace php\util;

use Countable;
use Traversable;

/**
 * Class SharedCollection
 * @package php\util
 */
abstract class SharedCollection extends SharedMemory implements Countable, Traversable
{
    const __PACKAGE__ = 'std, core';

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