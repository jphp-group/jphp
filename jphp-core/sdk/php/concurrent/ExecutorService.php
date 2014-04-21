<?php
namespace php\concurrent;

use php\lang\Environment;

/**
 * Class ExecutorService
 * @package php\concurrent
 */
class ExecutorService {
    /**
     * internal
     */
    private function __construct() { }

    /**
     * @return bool
     */
    public function isScheduled() { }

    /**
     * @return bool
     */
    public function isShutdown() { }

    /**
     * @return bool
     */
    public function isTerminated() { }

    /**
     * @param callable $runnable
     * @param Environment $env
     */
    public function execute(callable $runnable, Environment $env = null) { }

    /**
     * @param callable $runnable
     * @param Environment $env
     * @return Future
     */
    public function submit(callable $runnable, Environment $env = null) { }

    /**
     * @param callable $runnable
     * @param int $delay - milliseconds
     * @param Environment $env
     * @return Future
     */
    public function schedule(callable $runnable, $delay, Environment $env = null) { }

    /**
     * Initiates an orderly shutdown in which previously submitted
     * tasks are executed, but no new tasks will be accepted.
     */
    public function shutdown() { }

    /**
     * Attempts to stop all actively executing tasks, halts the
     * processing of waiting tasks
     */
    public function shutdownNow() { }

    /**
     * Blocks until all tasks have completed execution after a shutdown
     * request, or the timeout occurs, or the current thread is
     * interrupted, whichever happens first.
     *
     * @param int $timeout - in milliseconds
     * @return bool
     * @throws \Exception
     */
    public function awaitTermination($timeout) { }

    /**
     * @param int $max
     * @return ExecutorService
     */
    public static function newFixedThreadPool($max) { }

    /**
     * @return ExecutorService
     */
    public static function newCachedThreadPool() { }

    /**
     * Creates an Executor that uses a single worker thread operating
     * off an unbounded queue.
     * @return ExecutorService
     */
    public static function newSingleThreadExecutor() { }

    /**
     * Creates a thread pool that can schedule commands to run after a
     * given delay, or to execute periodically.
     *
     * @param int $corePoolSize
     * @return ExecutorService
     */
    public static function newScheduledThreadPool($corePoolSize) { }
}
