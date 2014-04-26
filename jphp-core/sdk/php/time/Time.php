<?php
namespace php\time;

/**
 * Class Time, Immutable
 * @package php\time
 */
class Time {
    /**
     * @param int $date timestamp
     * @param null|TimeZone $timezone - if null then gets default timezone
     */
    public function __construct($date, TimeZone $timezone = null) { }

    /**
     * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
     * represented by this Time object.
     *
     * @return int
     */
    public function getTime() { return 0; }

    /**
     * Get timezone of the time object
     * @return TimeZone
     */
    public function getTimeZone() { return new TimeZone(0, ''); }

    /**
     * @return int
     */
    public function year() { return 0; }

    /**
     * @return int
     */
    public function month() { return 0; }

    /**
     * Get week of year
     * @return int
     */
    public function week() { return 0; }

    /**
     * Get week of month
     * @return int
     */
    public function weekOfMonth() { return 0; }

    /**
     * Get day of year
     * @return int
     */
    public function day() { return 0; }

    /**
     * Get day of month
     * @return int
     */
    public function dayOfMonth() { return 0; }

    /**
     * Get day of week
     * @return int
     */
    public function dayOfWeek() { return 0; }

    /**
     * @return int
     */
    public function dayOfWeekInMonth() { return 0; }

    /**
     * Get a new time + day count
     * @param int $count
     * @return Time
     */
    public function plusDays($count) { return new Time(0); }

    /**
     * Get a new time + month count
     * @param int $count
     * @return Time
     */
    public function plusMonths($count) { return new Time(0); }

    /**
     * Get a new time + year count
     * @param int $count
     * @return Time
     */
    public function plusYears($count) { return new Time(0); }

    /**
     * Returns now time object
     * @param TimeZone $timeZone
     * @return Time
     */
    public static function now(TimeZone $timeZone = null) { return new Time(0); }

    /**
     * Create a new time by using the $args arrays that can contain the ``sec``, ``min``, ``hour`` and other keys::
     *
     *    $time = Time::of(['year' => 2013, 'month' => 1, 'day' => 1]) // 01 Jan 2013
     *
     * @param array $args [sec, min, hour, day, month, year]
     * @param TimeZone $timeZone if null then it uses the default timezone
     * @return Time
     */
    public static function of(array $args, TimeZone $timeZone = null) { return new Time(0); }

    /**
     * Returns the current time in milliseconds.  Note that
     * while the unit of time of the return value is a millisecond,
     * the granularity of the value depends on the underlying
     * operating system and may be larger.  For example, many
     * operating systems measure time in units of tens of
     * milliseconds.
     *
     * @return int
     */
    public static function millis() { return 0; }

    /**
     * Returns the current value of the running Java Virtual Machine's
     * high-resolution time source, in nanoseconds.
     *
     * This method can only be used to measure elapsed time and is
     * not related to any other notion of system or wall-clock time.
     * The value returned represents nanoseconds since some fixed but
     * arbitrary *origin* time (perhaps in the future, so values
     * may be negative).  The same origin is used by all invocations of
     * this method in an instance of a Java virtual machine; other
     * virtual machine instances are likely to use a different origin
     *
     * @return int
     */
    public static function nanos() { return 0; }
}