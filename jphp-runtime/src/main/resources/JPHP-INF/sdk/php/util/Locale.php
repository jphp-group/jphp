<?php
namespace php\util;

/**
 * Class Locale, Immutable
 * @package php\util
 * @packages std, core
 */
class Locale
{


    /**
     * @param string $lang
     * @param string $country
     * @param string $variant
     */
    public function __construct($lang, $country = '', $variant = '') { }

    /**
     * @return string
     */
    public function getLanguage() { return ''; }

    /**
     * @param Locale $locale
     * @return string
     */
    public function getDisplayLanguage(Locale $locale = null) { return ''; }

    /**
     * @return string
     */
    public function getCountry() { return ''; }

    /**
     * @param Locale $locale
     * @return string
     */
    public function getDisplayCountry(Locale $locale = null) { return ''; }

    /**
     * @return string
     */
    public function getVariant() { return ''; }

    /**
     * @param Locale $locale
     * @return string
     */
    public function getDisplayVariant(Locale $locale = null) { return ''; }

    /**
     * @return string
     */
    public function getISO3Country() { return ''; }

    /**
     * @return string
     */
    public function getISO3Language() { return ''; }

    /**
     * Returns a string representation of this Locale
     * object, consisting of language, country, variant, script,
     * and extensions as below::
     *
     *     language + "_" + country + "_" + (variant + "_#" | "#") + script + "-" + extensions
     *
     *
     * @return string
     */
    public function __toString() { return ''; }

    /**
     * Class is immutable, the disallowed clone method
     */
    private function __clone() { }

    /**
     * @return Locale
     */
    public static function ENGLISH() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function US() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function UK() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function CANADA() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function CANADA_FRENCH() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function FRENCH() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function FRANCE() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function ITALIAN() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function ITALY() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function GERMAN() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function GERMANY() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function JAPAN() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function JAPANESE() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function KOREA() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function KOREAN() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function CHINA() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function CHINESE() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function TAIWAN() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function RUSSIAN() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function RUSSIA() { return new Locale(0); }

    /**
     * @return Locale
     */
    public static function ROOT() { return new Locale(0); }

    /**
     * Get default locale (if globally = false - only for the current environment)
     * @param bool $globally
     * @return Locale
     */
    public static function getDefault($globally = false) { return new Locale(''); }

    /**
     * Set default locale
     * @param Locale $locale
     * @param bool $globally if ``false`` - only for the current environment
     */
    public static function setDefault(Locale $locale, $globally = false) {  }

    /**
     * Returns an array of all installed locales.
     * The returned array represents the union of locales supported
     * by the Java runtime environment
     *
     * @return Locale[] An array of installed locales.
     */
    public static function getAvailableLocales() { return []; }
}
