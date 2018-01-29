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
     * Create trigger timer which once call when trigger callback will return true.
     *
     * @param callable $trigger (): bool
     * @param callable $callback
     * @param int $checkPeriod time period of checking trigger (default 1/60 sec).
     * @return Timer
     */
    static function trigger(callable $trigger, callable $callback, $checkPeriod = 16)
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
     * Converts a string period to amount of millis.
     * 
     * '' - millis
     *  's' - seconds
     *  'm' - minutes
     *  'h' - hours
     *  'd' - days (24 hours)
     *
     *  for example '2h 30m 10s' or '2.5s' or '2000' or '1m 30s'
     *
     * @param string $value
     * @return int
     */
    static function parsePeriod($value)
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