<?php
namespace php\util;

abstract class SharedMemory
{
    const __PACKAGE__ = 'std, core';

    /**
     * You can use a shared value as a mutex
     * @param callable $callback (SharedValue $this)
     * @return mixed result of execution of $callback
     */
    public function synchronize(callable $callback)
    {
    }
}