<?php
namespace php\lib;
use php\lib\regex\Pattern;

/**
 * Utilities for regular expressions
 *
 *  http://www.regular-expressions.info/java.html
 *
 * Class regex
 * @package php\lib
 */
class regex {

    private function __construct() { }

    /**
     * Tells whether or not this string matches the given regular expression.
     * See also java.lang.String.matches()
     *
     * @param string $string
     * @param string $pattern  regular expression
     * @return bool
     */
    public static function match($pattern, $string) { return false; }

    /**
     * Splits this string around matches of the given regular expression.
     * See also java.lang.String.split()
     *
     * @param string $string
     * @param string $pattern  the delimiting regular expression
     * @param int $limit  the result threshold
     * @return array the array of strings computed by splitting this string around matches of the given regular expression
     */
    public static function split($pattern, $string, $limit = 0) { return []; }

    /**
     * Returns a literal pattern ``String`` for the specified
     * ``String``.
     *
     *
     * This method produces a ``String`` that can be used to
     * create a ``Pattern`` that would match the string
     * ``$string`` as if it were a literal pattern. Metacharacters
     * or escape sequences in the input sequence will be given no special
     * meaning.
     *
     * @param string $string The string to be literalized
     * @return string A literal string replacement
     */
    public static function quote($string) { return ''; }

    /**
     * Returns a literal replacement ``String`` for the specified
     * ``String``.
     *
     * This method produces a ``String`` that will work
     * as a literal replacement $string in the
     * replaceWithCallback() method of the ``php\lib\regex\Pattern`` class.
     * The ``String`` produced will match the sequence of characters
     * in $string treated as a literal sequence. Slashes ('\') and
     * dollar signs ('$') will be given no special meaning.
     *
     * @param string $string
     * @return string
     */
    public static function quoteReplacement($string) { return ''; }

    /**
     * Creates a new Pattern of regex. Alias of php\lib\regex\Pattern::of()
     *
     * @param string $pattern regular expression
     * @param string $string
     * @param int $flag
     * @return Pattern
     */
    public static function of($pattern, $string = '', $flag = 0) { return Pattern::of($pattern, $string, $flag); }
}
