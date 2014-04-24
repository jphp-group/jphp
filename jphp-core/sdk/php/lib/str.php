<?php
namespace php\lib;

/**
 * Class str
 * @package php\lib
 */
class str {

    private function __construct() {}

    /**
     * @param string $char
     * @return int
     */
    public static function ord($char) { return 0; }

    /**
     * @param int $code
     * @return string
     */
    public static function char($code) { return ''; }

    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring, starting at the specified index.
     *
     * @param string $string
     * @param string $search the substring to search for
     * @param int $fromIndex the index from which to start the search.
     * @return int - returns -1 if not found
     */
    public static function pos($string, $search, $fromIndex = 0) { return 0; }

    /**
     * The same method as ``pos()`` only with ignoring case characters
     *
     * @param string $string
     * @param string $search the substring to search for.
     * @param int $fromIndex the index from which to start the search.
     * @return int - returns -1 if not found
     */
    public static function posIgnoreCase($string, $search, $fromIndex = 0) { return 0; }

    /**
     * Returns the index within this string of the last occurrence of the
     * specified substring. The last occurrence of the empty string ""
     * is considered to occur at the index value ``$string.length``.
     *
     * @param string $string
     * @param string $search the substring to search for.
     * @param null|int $fromIndex - null means $fromIndex will be equal $string.length
     * @return int - returns -1 if not found
     */
    public static function lastPos($string, $search, $fromIndex = null) { return 0; }

    /**
     * The same method as ``lastPos()`` only with ignoring case characters
     *
     * @param string $string
     * @param string $search the substring to search for.
     * @param null|int $fromIndex - null means $fromIndex will be equal $string.length
     * @return int
     */
    public static function lastPosIgnoreCase($string, $search, $fromIndex = null) { return 0; }

    /**
     * Returns a new string that is a substring of this string. The
     * substring begins at the specified ``$beginIndex`` and
     * extends to the character at index ``$endIndex`` - 1.
     * Thus the length of the substring is ``endIndex - beginIndex``.
     *
     * @param string $string
     * @param int $beginIndex
     * @param null|int $endIndex When ``$endIndex`` equals to ``null`` then it will be equal ``$string.length``
     * @return string - return false if params are invalid
     */
    public static function sub($string, $beginIndex, $endIndex = null) { return ''; }

    /**
     * Compares two strings lexicographically.
     * The comparison is based on the Unicode value of each character in
     * the strings.
     *
     * The character sequence represented by ``$string1``
     * ``String`` is compared lexicographically to the
     * character sequence represented by ``$string2``. The result is
     * a negative integer if ``$string1``
     * lexicographically precedes ``$string2``. The result is a
     * positive integer if ``$string1`` lexicographically
     * follows ``$string2``. The result is zero if the strings
     * are equal; ``compare`` returns **0** exactly when
     * the strings are equal
     *
     * @param string $string1 - first string
     * @param string $string2 - second string
     * @return int
     */
    public static function compare($string1, $string2) { return 0; }

    /**
     * The same method as ``compare()`` only with ignoring case characters
     *
     * @param string $string1
     * @param string $string2
     * @return int
     */
    public static function compareIgnoreCase($string1, $string2) { return 0; }

    /**
     * Checks that the strings are equal with ignoring case characters
     *
     * @param string $string1
     * @param string $string2
     * @return bool
     */
    public static function equalsIgnoreCase($string1, $string2) { return false; }

    /**
     * Tests if the substring of this string beginning at the
     * specified index starts with the specified prefix.
     *
     * Returns ```true`` if the character sequence represented by the
     *          argument is a prefix of the substring of this object starting
     *          at index ``offset``; ``false`` otherwise.
     *          The result is ``false`` if ``toffset`` is
     *          negative or greater than the length of this
     *          ``$string``; otherwise the result is the same
     *          as the result of the expression
     *
     *          .. code-block:: php
     *
     *              startsWith(sub($offset), $prefix)
     *
     * @param string $string
     * @param string $prefix
     * @param int $offset where to begin looking in this string
     * @return bool
     */
    public static function startsWith($string, $prefix, $offset = 0) { return false; }

    /**
     * Tests if this string ends with the specified suffix.
     *
     * @param string $string
     * @param string $suffix
     * @return bool
     */
    public static function endsWith($string, $suffix) { return false; }

    /**
     * Converts all of the characters in ``$string`` to lower
     * case using the rules of the default locale.
     *
     * @param string $string
     * @return string
     */
    public static function lower($string) { return ''; }

    /**
     * Converts all of the characters in ``$string`` to upper
     * case using the rules of the default locale.
     *
     * @param string $string
     * @return string
     */
    public static function upper($string) { return ''; }

    /**
     * Returns the length of ``$string``.
     * The length is equal to the number of `Unicode code units` in the string.
     *
     * @param string $string
     * @return int
     */
    public static function length($string) { return 0; }

    /**
     * Replaces each substring of this string that matches the literal target
     * sequence with the specified literal replacement sequence. The
     * replacement proceeds from the beginning of the string to the end, for
     * example, replacing "aa" with "b" in the string "aaa" will result in
     * "ba" rather than "ab".
     *
     * @param string $string
     * @param string $target The sequence of char values to be replaced
     * @param string $replacement The replacement sequence of char values
     * @return string
     */
    public static function replace($string, $target, $replacement) { return ''; }

    /**
     * Return s a new string consisting of the original ``$string`` repeated
     *
     * @param string $string
     * @param int $amount number of times to repeat str
     * @return string
     */
    public static function repeat($string, $amount) { return ''; }

    /**
     * Returns a copy of the string, with leading and trailing whitespace
     * omitted.
     *
     * @param string $string
     * @return string
     */
    public static function trim($string) { return ''; }

    /**
     * @param string $string
     * @return string
     */
    public static function reverse($string) { return ''; }

    /**
     * Returns a randomized string based on chars in $string
     * @param string $string
     * @return string
     */
    public static function shuffle($string) { return ''; }

    /**
     * The method like ``explode()`` in Zend PHP
     *
     * @param string $string
     * @param string $separator
     * @param int $limit
     * @return array
     */
    public static function split($string, $separator, $limit = 0) { return []; }

    /**
     * The method like ``implode()`` in Zend PHP
     *
     * @param array|\Iterator $iterable
     * @param string $separator
     * @param int $limit
     * @return string
     */
    public static function join($iterable, $separator, $limit = 0) { return ''; }
}
