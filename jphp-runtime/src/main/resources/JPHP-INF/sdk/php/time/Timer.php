<?php
namespace php\time;

/**
 * Class Timer
 * @package php\time
 *
 * @packages std, core
 */
class Timer
{
    /**
     * Timer constructor.
     */
    protected function __construct()
    {
    }

    /**
     * Run once after the period in format:
     *  '' - millis
     *  's' - seconds
     *  'm' - minutes
     *  'h' - hours
     *  'd' - days (24 hours)
     *
     *  for example '2h 30m 10s' or '2.5s' or '2000' or '1m 30s'
     * 
     * @param string $period
     * @param callable $callback
     * @return Timer
     */
    static function after($period, callable $callback)
    {
    }

    /**
     * Run every time based on the period as in after() method.
     * 
     * @param string $period
     * @param callable $callback
     * @return Timer
     */
    static function every($period, callable $callback)
    {
    }

    /**
     * Like in JS.
     * 
     * @param callable $taskCallback
     * @param int $millis delay period in millis.
     * @return Timer
     */
    static function setTimeout(callable $taskCallback, $millis)
    {
    }

    /**
     * Like in JS.
     * @param callable $taskCallback
     * @param int $millis
     * @return Timer
     */
    static function setInterval(callable $taskCallback, $millis)
    {
    }


    /**
     * Call all timer tasks which scheduled.
     */
    static function cancelAll()
    {
    }

    /**
     * Cancel timer task.
     */
    function cancel()
    {
    }

    /**
     * Run timer task.
     */
    function run()
    {
    }

    /**
     * Returns the scheduled execution time of the most recent
     * actual execution of this task.  (If this method is invoked
     * while task execution is in progress, the return value is the scheduled
     * execution time of the ongoing task execution.)
     */
    function scheduledTime()
    {
    }
}