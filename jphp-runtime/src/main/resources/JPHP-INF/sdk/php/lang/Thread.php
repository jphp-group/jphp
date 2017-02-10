<?php
namespace php\lang;

/**
 * Class Thread
 * @packages std, core
 */
class Thread
{


    const MAX_PRIORITY = 10;
    const MIN_PRIORITY = 1;
    const NORM_PRIORITY = 5;

    /**
     * @param callable $runnable
     * @param Environment $env
     * @param ThreadGroup $group
     */
    public function __construct(callable $runnable, Environment $env = null, ThreadGroup $group = null)
    {
    }

    /**
     * @return int
     */
    public function getId()
    {
    }

    /**
     * @return string
     */
    public function getName()
    {
    }

    /**
     * @param string $value
     */
    public function setName($value)
    {
    }

    /**
     * @return ThreadGroup
     */
    public function getGroup()
    {
    }

    /**
     * @return bool
     */
    public function isDaemon()
    {
    }

    /**
     * @param bool $value
     */
    public function setDaemon($value)
    {
    }

    /**
     * @return bool
     */
    public function isInterrupted()
    {
    }

    /**
     * @return bool
     */
    public function isAlive()
    {
    }

    /**
     * start
     */
    public function start()
    {
    }

    /**
     * run
     */
    public function run()
    {
    }

    /**
     * Interrupts this thread.
     */
    public function interrupt()
    {
    }

    /**
     * Waits at most $millis milliseconds plus
     * $nanos nanoseconds for this thread to die.
     * @param int $millis
     * @param int $nanos
     */
    public function join($millis = 0, $nanos = 0)
    {
    }

    /**
     * A hint to the scheduler that the current thread is willing to yield
     * its current use of a processor. The scheduler is free to ignore this
     * hint.
     */
    public static function doYield()
    {
    }

    /**
     * Causes the currently executing thread to sleep (temporarily cease
     * execution)
     * @param int $millis
     * @param int $nanos
     */
    public static function sleep($millis, $nanos = 0)
    {
    }

    /**
     * @return int
     */
    public static function getActiveCount()
    {
    }

    /**
     * Get current thread
     * @return Thread
     */
    public static function current()
    {
    }
}
