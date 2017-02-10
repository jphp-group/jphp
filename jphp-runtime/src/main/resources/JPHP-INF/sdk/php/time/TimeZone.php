<?php
namespace php\time;

/**
 * Class TimeZone, Immutable
 * @package php\time
 * @packages std, core
 */
class TimeZone
{


    /**
     * @param int $rawOffset
     * @param string $ID
     * @param array $options
     * @return TimeZone
     */
    public function __construct($rawOffset, $ID, array $options = null) { }

    /**
     * Get id of the timezone
     * @return string
     */
    public function getId() { return ''; }

    /**
     * Get raw offset of the timezone
     * @return string
     */
    public function getRawOffset() { return ''; }

    /**
     * Class is immutable, the disallowed clone method
     */
    private function __clone() { }

    /**
     * Returns UTC Time zone
     * @return TimeZone
     */
    public static  function UTC() { return new TimeZone(0, ''); }

    /**
     * @param string $ID code of timezone, e.g.: 'UTC'
     * @return TimeZone
     */
    public static function of($ID) { return new TimeZone(0, ''); }

    /**
     * Set default time zone for Time objects, by default - the default timezone is UTC
     * @param TimeZone $zone
     * @param bool $globally
     */
    public static function setDefault(TimeZone $zone, $globally = false) { }

    /**
     * Get default timezone
     * @param bool $globally if ``false`` - only for the current environment
     * @return TimeZone
     */
    public static function getDefault($globally = false) { return new TimeZone(0, ''); }

    /**
     * Returns all available ids of timezones
     *
     * @param int|null $rawOffset
     * @return string[]
     */
    public static function getAvailableIDs($rawOffset = null) { return []; }
}
