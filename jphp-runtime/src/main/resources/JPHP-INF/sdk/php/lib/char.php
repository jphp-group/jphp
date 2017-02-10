<?php
namespace php\lib;

/**
 * Char Utils for working with unicode chars
 *      (using string[0] char)
 *
 * Class char
 * @packages std, core
 */
class char
{


    private function __construct() { }

    /**
     * @param int $code
     * @return string
     */
    public static function of($code) { return ''; }

    /**
     * @param string $char
     * @return int
     */
    public static function ord($char) { return 0; }

    /**
     * Determines the number of {@code char} values needed to
     * represent the specified character (Unicode code point). If the
     * specified character is equal to or greater than 0x10000, then
     * the method returns 2. Otherwise, the method returns 1.
     *
     * @param int $code
     * @return int
     */
    public static function count($code) { return 0; }

    /**
     * @param string $char1
     * @param string $char2
     * @return int
     */
    public static function compare($char1, $char2) { return 0; }

    /**
     * @param string $char
     * @return string
     */
    public static function lower($char) { return ''; }

    /**
     * @param string $char
     * @return string
     */
    public static function upper($char) { return ''; }

    /**
     * @param string $char
     * @return string
     */
    public static function title($char) { return ''; }

    /**
     * @param string $char
     * @return bool
     */
    public static function isSpace($char) { return false; }

    /**
     * @param string $char
     * @return bool
     */
    public static function isDigit($char) { return false; }

    /**
     * @param string $char
     * @return bool
     */
    public static function isLetter($char) { return false; }

    /**
     * @param string $char
     * @return bool
     */
    public static function isLetterOrDigit($char) { return false; }

    /**
     * @param string $char
     * @return bool
     */
    public static function isLower($char) { return false; }

    /**
     * @param string $char
     * @return bool
     */
    public static function isUpper($char) { return false; }

    /**
     * @param string $char
     * @return bool
     */
    public static function isTitle($char) { return false; }

    /**
     * @param string $char
     * @return bool
     */
    public static function isWhitespace($char) { return false; }

    /**
     * @param string $char
     * @return bool
     */
    public static function isISOControl($char) { return false; }

    /**
     * Determines if a character is defined in Unicode.
     * @param string $char
     * @return bool
     */
    public static function isDefined($char) { return false; }

    /**
     * Determines whether the specified character (Unicode code point)
     * is mirrored according to the Unicode specification.  Mirrored
     * characters should have their glyphs horizontally mirrored when
     * displayed in text that is right-to-left.
     *
     * @param string $char
     * @return bool
     */
    public static function isMirrored($char) { return false; }

    /**
     * Determines if the given $char value is a
     * <a href="http://www.unicode.org/glossary/#low_surrogate_code_unit">
     * Unicode low-surrogate code unit</a>
     * (also known as <i>trailing-surrogate code unit</i>).
     *
     * @param string $char
     * @return bool
     */
    public static function isLowSurrogate($char) { return false; }

    /**
     * Determines if the given $char value is a
     * <a href="http://www.unicode.org/glossary/#high_surrogate_code_unit">
     * Unicode high-surrogate code unit</a>
     * (also known as <i>leading-surrogate code unit</i>).
     *
     * @param string $char
     * @return bool
     */
    public static function isHighSurrogate($char) { return false; }

    /**
     * @param string $char
     * @return bool
     */
    public static function isPrintable($char) { return false; }

    /**
     * Returns the {@code int} value that the specified Unicode
     * character represents.
     *
     * @param string $char
     * @return int
     */
    public static function number($char) { return 0; }
}
