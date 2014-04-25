<?php
namespace php\lib\regex;

/**
 * http://www.regular-expressions.info/java.html
 *
 * Class Pattern
 * @package php\lib\regex
 */
class Pattern implements \Iterator {
    const CANON_EQ = 0x80;
    const CASE_INSENSITIVE = 0x02;
    const UNICODE_CASE = 0x40;
    const UNICODE_CHARACTER_CLASS = 0x100;
    const COMMENTS = 0x04;
    const DOTALL = 0x20;
    const LITERAL = 0x10;
    const MULTILINE = 0x08;
    const UNIX_LINES = 0x01;

    /**
     * Use ``php\lib\regex::pattern()`` instead of this
     */
    private function __construct() { }

    /**
     * Attempts to match the entire region against the pattern.
     *
     * @return bool
     */
    public function matches() { return false; }

    /**
     * Resets this matcher and then attempts to find the next subsequence of
     * the input sequence that matches the pattern, starting at the specified
     * index.
     *
     * @param int|null $start
     * @return bool
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
     */
    public function replace($replacement) { return ''; }

    /**
     * Replaces the first subsequence of the input sequence that matches the
     * pattern with the given replacement string.
     *
     * @param string $replacement
     * @return string
     */
    public function replaceFirst($replacement) { return ''; }

    /**
     * @param callable $callback (Pattern $pattern) -> string
     * @return string
     */
    public function replaceWithCallback(callable $callback) { return ''; }

    /**
     * Duplicates this pattern with a new $string
     *
     * @param string $string
     * @return Pattern
     */
    public function with($string) { return new Pattern(); }

    /**
     * Returns the input subsequence captured by the given group during the
     * previous match operation.
     *
     * @param null|int|float|string $group
     * @return string
     */
    public function group($group = null) { return ''; }

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
     */
    public function start($group = null) { return 0; }

    /**
     * Returns the offset after the last character matched.
     *
     * @param null|int $group
     * @return int
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
     * @return Pattern
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
     * Creates a new Pattern of regex with $string and $flag
     *
     * @param string $string
     * @param string $pattern regular expression
     * @param int $flag Pattern::CASE_INSENSITIVE and other constants
     * @return Pattern
     */
    public static function of($pattern, $string = '', $flag = 0) { return new Pattern(); }

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
}
