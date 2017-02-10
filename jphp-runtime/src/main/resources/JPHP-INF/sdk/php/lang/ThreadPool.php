<?php
namespace php\lang;

use php\concurrent\Future;

/**
 * Class ThreadPool
 * @packages std, core
 */
class ThreadPool
{


    /**
     * internal
     */
    private function __construct()
    {
    }

    /**
     * Is Scheduled ?
     * --RU--
     * Поставлен в расписание?
     *
     * @return bool
     */
    public function isScheduled()
    {
    }

    /**
     * Is Shutdown?
     * --RU--
     * Завершен?
     *
     * @return bool
     */
    public function isShutdown()
    {
    }

    /**
     * @return bool
     */
    public function isTerminated()
    {
    }

    /**
     * Execute some $runnable via the Executor Service
     * --RU--
     * Выполнить некоторый $runnable через данный сервис
     *
     * @param callable $runnable
     * @param Environment $env
     */
    public function execute(callable $runnable, Environment $env = null)
    {
    }

    /**
     * @param callable $runnable
     * @param Environment $env
     * @return Future
     */
    public function submit(callable $runnable, Environment $env = null)
    {
    }

    /**
     * @param callable $runnable
     * @param int $delay
     * @param Environment $env
     * @return Future
     */
    public function schedule(callable $runnable, $delay, Environment $env = null)
    {
    }

    /**
     * Initiates an orderly shutdown in which previously submitted
     * tasks are executed, but no new tasks will be accepted.
     * --RU--
     * Начинает попорядку завершать пердыдущие засабмиченные завершенные задания,
     * но не новые задания
     */
    public function shutdown()
    {
    }

    /**
     * Attempts to stop all actively executing tasks, halts the
     * processing of waiting tasks
     * --RU--
     * Пытается остановить все активные выполняющиеся задания, обрывает
     * обработку ожидания заданий
     */
    public function shutdownNow()
    {
    }

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
    public function awaitTermination($timeout)
    {
    }

    /**
     * @param int $coreSize the number of threads to keep in the pool, even if they are idle
     * @param int $maxSize the maximum number of threads to allow in the pool
     * @param int $keepAliveTime in millis
     * @return ThreadPool
     */
    public static function create($coreSize, $maxSize, $keepAliveTime = 0)
    {
    }

    /**
     * @param int $max
     * @return ThreadPool
     */
    public static function createFixed($max)
    {
    }

    /**
     * @return ThreadPool
     */
    public static function createCached()
    {
    }

    /**
     * Creates an Executor that uses a single worker thread operating
     * off an unbounded queue.
     * --RU--
     * Создает Executor, который будет все обрабатывать в один поток
     *
     * @return ThreadPool
     */
    public static function createSingle()
    {
    }

    /**
     * Creates a thread pool that can schedule commands to run after a
     * given delay, or to execute periodically.
     * --RU--
     * Создает пулл потоков, который сможет планировать задания к запуску
     * после определенной задержки или для переодического их запуска.
     *
     * @param int $corePoolSize
     * @return ThreadPool
     */
    public static function createScheduled($corePoolSize)
    {
    }
}