<?php
namespace php\util;

/**
 * http://www.regular-expressions.info/java.html
 *
 * Class Regex, Immutable
 * @package php\util
 * @packages std, core
 */
class Regex implements \Iterator
{


    const CANON_EQ = 0x80;
    const CASE_INSENSITIVE = 0x02;
    const UNICODE_CASE = 0x40;
    const COMMENTS = 0x04;
    const DOTALL = 0x20;
    const LITERAL = 0x10;
    const MULTILINE = 0x08;
    const UNIX_LINES = 0x01;

    /**
     * Regex of $pattern and $flag
     *
     * @param string $pattern regular expression
     * @param int|string $flag Regex::CASE_INSENSITIVE and other constants, or string "i m s etc."
     * @param string $string
     */
    public function __construct($pattern, $flag = 0, $string = "") { }

    /**
     * Get the current pattern.
     *
     * @return string
     */
    public function getPattern() { return ''; }

    /**
     * Get the input string.
     *
     * @return string
     */
    public function getInput() {}

    /**
     * Get the current flags
     * @return int
     */
    public function getFlags() { return 0; }

    /**
     * Creates a new Regex of regex with $string and $flag
     *
     * @param string $pattern regular expression
     * @param int|string $flag Regex::CASE_INSENSITIVE and other constants, or string "i m s etc."
     * @param string $string
     * @return Regex
     */
    public static function of($pattern, $flag = 0, $string = "") { }


    /**
     * Executes a search for a match between a regular expression and a specified string. Returns true or false.
     *
     * @param string $string
     * @return bool
     */
    public function test($string)
    {
    }

    /**
     * Attempts to match the entire region against the pattern.
     *
     * @return bool
     */
    public function matches() { return false; }

    /**
     * Returns array of all found groups.
     *
     * @param int $start
     * @return array
     */
    public function all($start = null)
    {
    }

    /**
     * Calls find() + groups() methods and returns the result of the groups() method.
     * Returns the first found groups, if founds nothing returns null.
     *
     * @param int $start
     * @return array|null
     */
    public function one($start = null)
    {
    }

    /**
     * Alias of one() method.
     *
     * @param int $start
     * @return array
     */
    public function first($start = null)
    {
    }

    /**
     * Finds the last match and returns last groups.
     *
     * @param int $start
     * @return array
     */
    public function last($start = null)
    {
    }

    /**
     * Resets this matcher and then attempts to find the next subsequence of
     * the input sequence that matches the pattern, starting at the specified
     * index.
     *
     * @param int|null $start
     * @return bool
     * @throws RegexException
     */
    public function find($start = null) { return false; }

    /**
     * Replaces every subsequence of the input sequence that matches the
     * pattern with the given replacement string.
     *
     * This method first resets this matcher.  It then scans the input
     * sequence looking for matches of the pattern.  Characters that are not
     * part of any match are appended directly to the result string; each match
     * is replaced in the result by the replacement string.
     *
     * @param string $replacement
     * @return string
     * @throws RegexException
     */
    public function replace($replacement) { return ''; }

    /**
     * Replaces the first subsequence of the input sequence that matches the
     * pattern with the given replacement string.
     *
     * @param string $replacement
     * @return string
     * @throws RegexException
     */
    public function replaceFirst($replacement) { return ''; }

    /**
     * @param int $group
     * @param string $replacement
     * @return string
     * @throws RegexException
     */
    public function replaceGroup($group, $replacement) { return ''; }

    /**
     * @param callable $callback (Regex $pattern) -> string
     * @return string
     * @throws RegexException
     */
    public function replaceWithCallback(callable $callback) { return ''; }

    /**
     * Duplicates this pattern with a new $string
     *
     * @param string $string
     * @return Regex
     */
    public function with($string) { return new Regex(); }

    /**
     * Clone this object with the new $flags
     *
     * @param int|string $flags
     * @return Regex
     */
    public function withFlags($flags) { return new Regex(); }

    /**
     * Returns an array of all group.
     *
     * @return array
     */
    public function groups() { return []; }

    /**
     * Returns the input subsequence captured by the given group during the
     * previous match operation.
     *
     * @param null|int $group
     * @return string
     * @throws RegexException
     */
    public function group($group = null) { return ''; }

    /**
     * Returns group names.
     *
     * @return array
     */
    public function groupNames() { }

    /**
     * Returns the number of capturing groups in this matcher's pattern.
     *
     * @return int
     */
    public function getGroupCount() { return 0; }

    /**
     * Returns the start index of the previous match.
     *
     * @param null|int $group
     * @return int
     * @throws RegexException
     */
    public function start($group = null) { return 0; }

    /**
     * Returns the offset after the last character matched.
     *
     * @param null|int $group
     * @return int
     * @throws RegexException
     */
    public function end($group = null) { return 0; }

    /**
     * Returns true if the end of input was hit by the search engine in
     * the last match operation performed by this matcher.
     *
     * @return bool
     */
    public function hitEnd() { return false; }

    /**
     * Returns true if more input could change a positive match into a
     * negative one.
     *
     * If this method returns true, and a match was found, then more
     * input could cause the match to be lost. If this method returns false
     * and a match was found, then more input might change the match but the
     * match won't be lost. If a match was not found, then requireEnd has no
     * meaning.
     *
     * @return bool
     */
    public function requireEnd() { return false; }

    /**
     * Attempts to match the input sequence, starting at the beginning of the
     * region, against the pattern.
     *
     * @return bool
     */
    public function lookingAt() { return false; }

    /**
     * Sets the limits of this matcher's region. The region is the part of the
     * input sequence that will be searched to find a match. Invoking this
     * method resets the matcher, and then sets the region to start at the
     * index specified by the $start parameter and end at the
     * index specified by the $end parameter.
     *
     * @param int $start
     * @param int $end
     * @return Regex
     * @throws RegexException
     */
    public function region($start, $end) { return $this; }

    /**
     * Reports the start index of this matcher's region. The
     * searches this matcher conducts are limited to finding matches
     * within ``regionStart()`` (inclusive) and
     * ``regionEnd()`` (exclusive).
     *
     * @return int
     */
    public function regionStart() { return 0; }

    /**
     * Reports the end index (exclusive) of this matcher's region.
     * The searches this matcher conducts are limited to finding matches
     * within ``regionStart()`` (inclusive) and
     * ``regionEnd()`` (exclusive).
     *
     * @return int
     */
    public function regionEnd() { return 0; }

    /**
     * Resets this matcher.
     *
     * Resetting a matcher discards all of its explicit state information
     * and sets its append position to zero. The matcher's region is set to the
     * default region, which is its entire character sequence. The anchoring
     * and transparency of this matcher's region boundaries are unaffected.
     *
     * @param null|string $string  The new input character sequence
     * @return $this
     */
    public function reset($string = null) { return $this; }

    /**
     * @return null|string
     */
    public function current() { return ''; }

    public function next() { }

    /**
     * @return int
     */
    public function key() { }

    /**
     * @return bool
     */
    public function valid() { }

    public function rewind() {}

    /**
     * Class is immutable, the disallowed clone method
     */
    private function __clone() { }

    /**
     * Tells whether or not this string matches the given regular expression.
     * See also java.lang.String.matches()
     *
     * @param string $pattern regular expression
     * @param string $string
     * @param int|string $flags
     * @return bool
     */
    public static function match($pattern, $string, $flags = 0) { return false; }

    /**
     * Splits this string around matches of the given regular expression.
     * See also java.lang.String.split()
     *
     * @param string $string
     * @param string $pattern  the delimiting regular expression
     * @param int $limit  the result threshold
     * @return array the array of strings computed by splitting this string around matches of the given regular expression
     * @throws RegexException
     */
    public static function split($pattern, $string, $limit = 0) { return []; }

    /**
     * Returns a literal pattern ``String`` for the specified
     * ``String``.
     *
     *
     * This method produces a ``String`` that can be used to
     * create a ``Regex`` that would match the string
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
     * replaceWithCallback() method of the ``php\util\Regex`` class.
     * The ``String`` produced will match the sequence of characters
     * in $string treated as a literal sequence. Slashes ('\') and
     * dollar signs ('$') will be given no special meaning.
     *
     * @param string $string
     * @return string
     */
    public static function quoteReplacement($string) { return ''; }
}
