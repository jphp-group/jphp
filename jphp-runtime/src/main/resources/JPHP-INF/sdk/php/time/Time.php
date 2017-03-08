<?php
namespace php\time;
use php\lang\JavaException;
use php\util\Locale;

/**
 * Class Time, Immutable
 * @package php\time
 * @packages std, core
 */
class Time
{


    /**
     * Create a new time with unix timestamp
     * @param int $date unix long timestamp (in millis), if is null, date will be as "now".
     * @param null|TimeZone $timezone - if null then gets default timezone
     * @param Locale $locale
     */
    public function __construct($date = null, TimeZone $timezone = null, Locale $locale = null) { }

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
     * Get the current year
     * @return int
     */
    public function year() { return 0; }

    /**
     * Get the current month of the year, 1 - Jan, 12 - Dec
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
     * Get hour, indicating the hour of the morning or afternoon.
     * hour() is used for the 12-hour clock (0 - 11). Noon and midnight are represented by 0, not by 12.
     *
     * @return int
     */
    public function hour() { return 0; }

    /**
     * Get hour of the day
     * @return int
     */
    public function hourOfDay() { return 0; }

    /**
     * Get minute of the hour
     * @return int
     */
    public function minute() { return 0; }

    /**
     * Get second of the minute
     * @return int
     */
    public function second() { return 0; }

    /**
     * Get millisecond of the second
     * @return int
     */
    public function millisecond() { return 0; }

    /**
     * Compares the time values
     *
     * Returns the value ``0`` if the time represented by the argument
     * is equal to the time represented by this ``Time``; a value
     * less than ``0`` if the time of this ``Time`` is
     * before the time represented by the argument; and a value greater than
     * ``0`` if the time of this ``Time`` is after the
     * time represented by the argument.
     *
     * @param Time $time
     * @return int
     */
    public function compare(Time $time) { return 0; }

    /**
     * @param TimeZone $timeZone
     * @return Time
     */
    public function withTimeZone(TimeZone $timeZone) { return new Time(0); }

    /**
     * @param Locale $locale
     * @return Time
     */
    public function withLocale(Locale $locale) { return new Time(0); }

    /**
     * Get a new time + $args
     *
     * .. note::
     *
     *    use negative values to minus
     *
     * @param array $args [millis, sec, min, hour, day, month, year]
     * @return Time
     */
    public function add(array $args) { return new Time(0); }

    /**
     * Clones the current datetime and replaces some fields to new values $args
     *
     * @param array $args [millis, sec, min, hour, day, month, year]
     * @return Time
     */
    public function replace(array $args) { return new Time(0); }

    /**
     * Format the current datetime to string with $format
     *
     *  - G    Era designator    Text    AD
     *  - y    Year    Year    1996; 96
     *  - M    Month in year    Month    July; Jul; 07
     *  - w    Week in year    Number    27
     *  - W    Week in month    Number    2
     *  - D    Day in year    Number    189
     *  - d    Day in month    Number    10
     *  - F    Day of week in month    Number    2
     *  - E    Day in week    Text    Tuesday; Tue
     *  - a    Am/pm marker    Text    PM
     *  - H    Hour in day (0-23)    Number    0
     *  - k    Hour in day (1-24)    Number    24
     *  - K    Hour in am/pm (0-11)    Number    0
     *  - h    Hour in am/pm (1-12)    Number    12
     *  - m    Minute in hour    Number    30
     *  - s    Second in minute    Number    55
     *  - S    Millisecond    Number    978
     *  - z    Time zone    General time zone    Pacific Standard Time; PST; GMT-08:00
     *  - Z    Time zone    RFC 822 time zone    -0800
     *
     * @param string $format date time format
     * @param Locale $locale
     * @return string
     */
    public function toString($format, Locale $locale = null) { return ''; }

    /**
     * Format the time to yyyy-MM-dd'T'HH:mm:ss
     * @return string
     */
    public function __toString() { return ''; }

    /**
     * Class is immutable, the disallowed clone method
     */
    private function __clone() { }

    /**
     * Returns now time object (date + time)
     * @param TimeZone $timeZone
     * @param Locale $locale
     * @return Time
     */
    public static function now(TimeZone $timeZone = null, Locale $locale = null) { return new Time(0); }

    /**
     * Returns today date (without time)
     * @param TimeZone $timeZone
     * @param Locale $locale
     * @return Time
     */
    public static function today(TimeZone $timeZone = null, Locale $locale = null)  { return new Time(0); }

    /**
     * Create a new time by using the $args arrays that can contain the ``sec``, ``min``, ``hour`` and other keys::
     *
     *    $time = Time::of(['year' => 2013, 'month' => 1, 'day' => 1]) // 01 Jan 2013
     *
     * @param array $args [millis, sec, min, hour, day, month, year]
     * @param TimeZone $timeZone if null then it uses the default timezone
     * @param Locale $locale
     * @return Time
     */
    public static function of(array $args, TimeZone $timeZone = null, Locale $locale = null) { return new Time(0); }

    /**
     * Returns the current time in seconds (like the ``millis()`` method only in seconds)
     *
     * @return int
     */
    public static function seconds() { return 0; }

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
