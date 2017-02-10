<?php

namespace php\time;

use php\util\Locale;

/**
 * Class TimeFormat, Immutable
 * @package php\time
 * @packages std, core
 */
class TimeFormat
{


    /**
     * @param string $format
     * @param Locale $locale if ``null`` then it uses the default locale
     * @param array $formatSymbols [months => [...], short_months, eras, weekdays, short_weekdays, local_pattern_chars]
     */
    public function __construct($format, Locale $locale = null, array $formatSymbols = null) { }

    /**
     * @param Time $time
     * @return string
     */
    public function format(Time $time) { return ''; }

    /**
     * @param string $string
     * @param TimeZone $timeZone
     * @return Time|null if parse error then returns ``null``
     */
    public function parse($string, TimeZone $timeZone = null) { return new Time(0); }

    /**
     * Class is immutable, the disallowed clone method
     */
    private function __clone() { }
}
