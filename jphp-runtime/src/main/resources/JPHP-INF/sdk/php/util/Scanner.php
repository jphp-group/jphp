<?php
namespace php\util;

use php\io\IOException;
use php\io\Stream;
use php\lang\IllegalArgumentException;

/**
 * A simple text scanner which can parse primitive types and strings using
 * regular expressions.
 *
 * Class Scanner
 * @package php\util
 * @packages std, core
 */
class Scanner implements \Iterator
{


    /**
     * @param string|Stream $source
     * @param string|null $charset e.g.: UTF-8, windows-1251, etc., only for Stream objects
     * @throws IllegalArgumentException if $charset is invalid
     */
    public function __construct($source, $charset = null) { }

    /**
     * @param Regex $pattern
     * @return bool
     */
    public function hasNext(Regex $pattern = null) { return false; }

    /**
     * @param Regex $pattern
     * @return string|null null if doesn't has the next pattern
     */
    public function next(Regex $pattern = null) { return ''; }

    /**
     * @return string|null null if doesn't has the next line
     */
    public function nextLine() { return ''; }

    /**
     * @return bool
     */
    public function hasNextLine() { return false; }

    /**
     * @param null|int $radix if null then uses the default radix
     * @return int|null null if doesn't has the next int
     */
    public function nextInt($radix = null) { return 0; }

    /**
     * @param null|int $radix if null then uses the default radix
     * @return bool
     */
    public function hasNextInt($radix = null) { return false; }

    /**
     * @return float|null null if does not has the next double
     */
    public function nextDouble() { return 0.0; }

    /**
     * @return bool
     */
    public function hasNextDouble() { return false; }

    /**
     * @param Regex $pattern
     * @return bool ``true`` on success, ``false`` on fail
     */
    public function skip(Regex $pattern) { return false; }

    /**
     * @param Regex $delimiter
     * @return Scanner
     */
    public function useDelimiter(Regex $delimiter) { return $this; }

    /**
     * @param Locale $locale
     * @return Scanner
     */
    public function useLocale(Locale $locale) { return $this; }

    /**
     * @param int $value
     * @return Scanner
     */
    public function useRadix($value) { return $this; }

    /**
     * Get the last io exception (if does not occur then returns ``null``)
     * @return IOException|null
     */
    public function getIOException() { return new IOException(); }

    /**
     * Resets this scanner.
     *
     * Resetting a scanner discards all of its explicit state
     * information which may have been changed by invocations of
     * ``useDelimiter()``, ``useLocale()``, or ``useRadix()``.
     */
    public function reset() { }

    /**
     * Uses the result of the last called ``next()`` method
     * @return string
     */
    public function current() { return ''; }

    /**
     * @return int
     */
    public function key() { return 0; }

    /**
     * @return bool
     */
    public function valid() { return false; }

    /**
     * Iterator of the scanner can be used only one time
     */
    public function rewind() {  }

    /**
     * The scanner cannot be cloned
     */
    private function __clone() { }
}
