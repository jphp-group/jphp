<?php
namespace php\concurrent;

use php\lang\Environment;

/**
 * Class ExecutorService
 * --RU--
 * Класс ExecutorService
 *
 * @package php\concurrent
 */
class ExecutorService {
    /**
     * internal
     */
    private function __construct() { }

    /**
     * Is Scheduled ?
     * --RU--
     * Поставлен в расписание?
     *
     * @return bool
     */
    public function isScheduled() { }

    /**
     * Is Shutdown?
     * --RU--
     * Завершен?
     *
     * @return bool
     */
    public function isShutdown() { }

    /**
     * @return bool
     */
    public function isTerminated() { }

    /**
     * Execute some $runnable via the Executor Service
     * --RU--
     * Выполнить некоторый $runnable через данный сервис
     *
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
     * @param int $delay
     * @param Environment $env
     * @return Future
     */
    public function schedule(callable $runnable, $delay, Environment $env = null) { }

    /**
     * Initiates an orderly shutdown in which previously submitted
     * tasks are executed, but no new tasks will be accepted.
     * --RU--
     * Начинает попорядку завершать пердыдущие засабмиченные завершенные задания,
     * но не новые задания
     */
    public function shutdown() { }

    /**
     * Attempts to stop all actively executing tasks, halts the
     * processing of waiting tasks
     * --RU--
     * Пытается остановить все активные выполняющиеся задания, обрывает
     * обработку ожидания заданий
     */
    public function shutdownNow() { }

    /**
     * Blocks until all tasks have completed execution after a shutdown
     * request, or the timeout occurs, or the current thread is
     * interrupted, whichever happens first.
     * --RU--
     * Блокирует до тех пор пока все задания не будут выполнены после запроса shutdown
     * или пока не случится timeout, или текущий поток не будет оборван.
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
     * --RU--
     * Создает Executor, который будет все обрабатывать в один поток
     *
     * @return ExecutorService
     */
    public static function newSingleThreadExecutor() { }

    /**
     * Creates a thread pool that can schedule commands to run after a
     * given delay, or to execute periodically.
     * --RU--
     * Создает пулл потоков, который сможет планировать задания к запуску
     * после определенной задержки или для переодического их запуска.
     *
     * @param int $corePoolSize
     * @return ExecutorService
     */
    public static function newScheduledThreadPool($corePoolSize) { }
}
