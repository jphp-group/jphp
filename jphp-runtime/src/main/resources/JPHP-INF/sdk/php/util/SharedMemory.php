<?php
namespace php\util;

/**
 * Class SharedMemory
 * @package php\util
 * @packages std, core
 */
abstract class SharedMemory
{


    /**
     * You can use a shared value as a mutex
     * @param callable $callback (SharedValue $this)
     * @return mixed result of execution of $callback
     */
    public function synchronize(callable $callback)
    {
    }
}