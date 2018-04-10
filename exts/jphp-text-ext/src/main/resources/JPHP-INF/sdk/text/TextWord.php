<?php
namespace text;
use php\lang\IllegalArgumentException;
use php\util\Locale;

/**
 * Class TextWord
 * @package text
 */
class TextWord
{
    /**
     * TextWord constructor.
     * @param string $text
     */
    public function __construct(string $text)
    {
    }

    public function __debugInfo(): array
    {
    }

    /**
     * Get length of text.
     * @return int
     */
    public function length(): int
    {
    }

    /**
     * Capitalizes all the delimiter separated words in a String.
     * Only the first character of each word is changed.
     *
     * @param string $delimiters
     * @return TextWord
     */
    public function capitalize(string $delimiters = null): TextWord
    {
    }

    /**
     * Converts all the delimiter separated words in a String into capitalized words,
     * that is each word is made up of a titlecase character and then a series of
     * lowercase characters.
     *
     * @param string|null $delimiters
     * @return TextWord
     */
    public function capitalizeFully(string $delimiters = null): TextWord
    {
    }

    /**
     * Uncapitalizes all the whitespace separated words in a String.
     * Only the first character of each word is changed.
     *
     * @param string|null $delimiters
     * @return TextWord
     */
    public function uncapitalize(string $delimiters = null): TextWord
    {
    }

    /**
     * Wraps a single line of text, identifying words by ' '.
     *
     * Leading spaces on a new line are stripped.
     * Trailing spaces are not stripped.
     *
     * @param int $lineLength
     * @param null|string $newLineString
     * @param bool $wrapLongWords
     * @return TextWord
     */
    public function wrap(int $lineLength, ?string $newLineString = null, bool $wrapLongWords = false): TextWord
    {
    }

    /**
     * Swaps the case of a String using a word based algorithm.
     *
     * @return TextWord
     */
    public function swapCase(): TextWord
    {
    }

    /**
     * Extracts the initial characters from each word in the String.
     *
     *  Ben John Lee => BJL
     *  Ben J.Lee => BJ
     *  Ben J.Lee => with delimiters '. ' => BJL
     *
     * @param string|null $delimiters
     * @return TextWord
     */
    public function initials(string $delimiters = null): TextWord
    {
    }

    /**
     * Find the Levenshtein distance between two Strings.
     * A higher score indicates a greater distance.
     *
     * @param string $other
     * @param int|null $threshold
     * @return int|null result distance, or -1
     */
    public function levenshteinDistance(string $other, ?int $threshold = null): ?int
    {
    }

    /**
     * Find the Jaro Winkler Distance which indicates the similarity score
     * between two CharSequences.
     *
     * @param string $other
     * @return float|null
     */
    public function jaroWinklerDistance(string $other): ?float
    {
    }

    /**
     * Calculates Jaccard distance of two set character sequence passed as
     * input. Calculates Jaccard similarity and returns the complement of it.
     *
     * @param string $other
     * @return float|null
     */
    public function jaccardDistance(string $other): ?float
    {
    }

    /**
     * Find the Hamming Distance between two strings with the same
     * length.
     *
     * The distance starts with zero, and for each occurrence of a
     * different character in either String, it increments the distance
     * by 1, and finally return its value.
     *
     * Since the Hamming Distance can only be calculated between strings of equal length, input of different lengths
     * will throw IllegalArgumentException
     *
     * @param string $other
     * @return int|null
     * @throws IllegalArgumentException
     */
    public function hammingDistance(string $other): ?int
    {
    }

    /**
     * Measures the cosine distance between two character sequences.
     *
     * For further explanation about Cosine Similarity and Cosine Distance, refer to
     * http://en.wikipedia.org/wiki/Cosine_similarity.
     *
     * @param string $other
     * @return float|null
     */
    public function cosineDistance(string $other): ?float
    {
    }

    /**
     * Find the Fuzzy Score which indicates the similarity score between two
     * Strings.
     *
     * A matching algorithm that is similar to the searching algorithms implemented in editors such
     * as Sublime Text, TextMate, Atom and others.
     *
     * One point is given for every matched character. Subsequent matches yield two bonus points. A higher score
     * indicates a higher similarity.
     *
     * @param string $score
     * @param null|Locale $locale
     * @return int
     */
    public function fuzzyScore(string $score, ?Locale $locale = null): int
    {
    }

    /**
     * Return text of this.
     *
     * @return string
     */
    public function __toString(): string
    {
    }

    public function __clone()
    {
    }
}