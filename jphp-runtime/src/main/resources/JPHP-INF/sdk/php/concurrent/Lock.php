<?php
namespace php\concurrent;
use php\lang\InterruptedException;

/**
 * Class Lock
 * @package php\concurrent
 */
class Lock
{
    /**
     * @param bool $fair
     */
    public function __construct($fair = false) {}

    /**
     * Acquires the lock.
     *
     * If the lock is not available then the current thread becomes
     * disabled for thread scheduling purposes and lies dormant until the
     * lock has been acquired.
     */
    public function lock() {}

    /**
     * @throws InterruptedException if the current thread is
     *         interrupted while acquiring the lock (and interruption
     *         of lock acquisition is supported).
     */
    public function lockInterruptibly() {}

    /**
     * Acquires the lock only if it is free at the time of invocation.
     *
     * @param int $timeout (optional) in millis
     * @return bool
     */
    public function tryLock($timeout) {}

    /**
     * Releases the lock.
     */
    public function unlock() {}
}