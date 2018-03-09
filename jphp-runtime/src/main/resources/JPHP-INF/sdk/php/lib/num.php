<?php
namespace php\lib;

/**
 *  Utils for numbers
 *
 * Class num
 * @package sdk\php\lib
 *
 * @packages std, core
 */
class num
{


    private function __construct() { }

    /**
     * Compare two numbers
     *
     * .. note:: it can be used as comparator for number sorting
     *
     * @param int|double $num1
     * @param int|double $num2
     * @return int 0 if are equal, 1 if $num1 > $num2, -1 if $num1 < $num2
     */
    public static function compare($num1, $num2) { return 0; }

    /**
     * Returns a string representation of the $number
     * argument as an unsigned integer in base 2.
     *
     * @param int $number
     * @return string
     */
    public static function toBin($number) { return ''; }

    /**
     * Returns a string representation of the $number
     * argument as an unsigned integer in base 8.
     *
     * @param int $number
     * @return string
     */
    public static function toOctal($number) { return ''; }

    /**
     * Returns a string representation of the $number
     * argument as an unsigned integer in base 16.
     *
     * @param int $number
     * @return string
     */
    public static function toHex($number) { return ''; }

    /**
     * Returns a string representation of the first argument in the
     * radix specified by the second argument.
     *
     * @param int $number
     * @param int $radix
     * @return string
     */
    public static function toString($number, $radix) { return ''; }

    /**
     * Returns the value obtained by reversing the order of the bits in the
     * two's complement binary representation of the specified {@code long}
     * value.
     *
     * @param int $number
     * @return int
     */
    public static function reverse($number) { return 0; }

    /**
     * Decodes a string into a integer.
     * Accepts decimal, hexadecimal, and octal numbers
     *
     * @param string $string
     * @return string or false if invalid number format
     */
    public static function decode($string) { return ''; }

    /**
     * @param int|double $number
     * @param string $pattern
     *          - e.g. if "###,###": 100500900 -> 100,500,900
     * @param string $decSep
     * @param string $groupSep
     * @return string
     */
    public static function format($number, $pattern, $decSep = '.', $groupSep = ',') { return ''; }
}