<?php
namespace php\concurrent;
use php\lang\InterruptedException;

/**
 * Class Semaphore
 * @package php\concurrent
 */
class Semaphore
{
    /**
     * @readonly
     * @var bool
     */
    public $fair;

    /**
     * @readonly
     * @var int
     */
    public $queueLength;

    /**
     * @readonly
     * @var int
     */
    public $availablePermits;

    /**
     * @readonly
     * @var int
     */
    public $drainPermits;

    /**
     * @param int $permits
     * @param bool $fair
     */
    public function __construct($permits, $fair = false) {}

    /**
     * @param int $permits
     * @param int $timeout (optional) in millis
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public function acquire($permits = 1, $timeout) {}

    /**
     * @param int $permits
     * @param int $timeout (optional) in millis
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public function acquireUninterruptibly($permits = 1, $timeout) {}

    /**
     * @return bool
     */
    public function hasQueuedThreads() {}

    /**
     * @param int $permits
     */
    public function release($permits = 1) {}

    /**
     * @param int $permits
     * @return bool
     */
    public function tryAcquire($permits = 1) {}
}