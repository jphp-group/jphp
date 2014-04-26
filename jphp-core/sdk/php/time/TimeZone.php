<?php
namespace php\time;

/**
 * Class TimeZone
 * @package php\time
 */
class TimeZone {
    /**
     * @param int $rawOffset
     * @param string $ID
     * @param array $options
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
     */
    public static function setDefault(TimeZone $zone) { }

    /**
     * Get default timezone
     * @return TimeZone
     */
    public static function getDefault() { return new TimeZone(0, ''); }

    /**
     * Get timezone of OS::
     *
     *      TimeZone::setDefault(TimeZone::getSystem()); // set the default timezone as the system timezone
     *
     * @return TimeZone
     */
    public static function getSystem() { return new TimeZone(0, ''); }

    /**
     * Returns all available ids of timezones
     *
     * @param int|null $rawOffset
     * @return string[]
     */
    public static function getAvailableIDs($rawOffset = null) { return []; }
}