<?php
namespace php\lib;

use php\format\ProcessorException;

/**
 * Class str
 * --RU--
 * Класс для работы со строками.
 * @packages std, core
 */
class str
{


    private function __construct()
    {
    }

    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring, starting at the specified index.
     * --RU--
     * Возвращает первую позицию найденной строки в подстроке, начиная
     * с указанной позиции ($fromIndex). Если ничего не найдено, возвращает -1.
     *
     * @param string $string
     * @param string $search the substring to search for
     * @param int $fromIndex the index from which to start the search.
     * @return int - returns -1 if not found
     */
    public static function pos($string, $search, $fromIndex = 0)
    {
        return 0;
    }

    /**
     * The same method as pos() only with ignoring case characters
     * --RU--
     * Метод такой же как pos() только с игнорированием регистра символов.
     *
     * @param string $string
     * @param string $search the substring to search for.
     * @param int $fromIndex the index from which to start the search.
     * @return int - returns -1 if not found
     */
    public static function posIgnoreCase($string, $search, $fromIndex = 0)
    {
        return 0;
    }

    /**
     * Returns the index within this string of the last occurrence of the
     * specified substring. The last occurrence of the empty string ""
     * is considered to occur at the index value $string.length.
     * --RU--
     * Возвращает позицию первой найденной подстроки в строке начиная с конца.
     * Если ничего не найдено, возвращает -1.
     *
     * @param string $string
     * @param string $search the substring to search for.
     * @param null|int $fromIndex - null means $fromIndex will be equal $string.length
     * @return int - returns -1 if not found
     */
    public static function lastPos($string, $search, $fromIndex = null)
    {
        return 0;
    }

    /**
     * The same method as ``lastPos()`` only with ignoring case characters
     * --RU--
     * Метод такой же как lastPos() только с игнорированием регистра символов.
     *
     * @param string $string
     * @param string $search the substring to search for.
     * @param null|int $fromIndex - null means $fromIndex will be equal $string.length
     * @return int
     */
    public static function lastPosIgnoreCase($string, $search, $fromIndex = null)
    {
        return 0;
    }

    /**
     * Returns a new string that is a substring of this string. The
     * substring begins at the specified ``$beginIndex`` and
     * extends to the character at index ``$endIndex`` - 1.
     * Thus the length of the substring is ``endIndex - beginIndex``.
     * --RU--
     * Возвращает подстроку из другой строки, которая укзывается через
     * начальную позицию (включительно) $beginIndex и до конечной позиции (не включительно) $endIndex.
     * В итоге длина подстроки будет равна $endIndex - $beginIndex.
     *
     * @param string $string
     * @param int $beginIndex
     * @param null|int $endIndex When ``$endIndex`` equals to ``null`` then it will be equal ``$string.length``
     * @return string - return false if params are invalid
     */
    public static function sub($string, $beginIndex, $endIndex = null)
    {
        return '';
    }

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
    public static function compare($string1, $string2)
    {
        return 0;
    }

    /**
     * The same method as ``compare()`` only with ignoring case characters
     *
     * @param string $string1
     * @param string $string2
     * @return int
     */
    public static function compareIgnoreCase($string1, $string2)
    {
        return 0;
    }

    /**
     * Checks that the strings are equal with ignoring case characters
     *
     * @param string $string1
     * @param string $string2
     * @return bool
     */
    public static function equalsIgnoreCase($string1, $string2)
    {
        return false;
    }

    /**
     * Tests if the substring of this string beginning at the
     * specified index starts with the specified prefix.
     *
     * Returns true if the character sequence represented by the
     *          argument is a prefix of the substring of this object starting
     *          at index `offset`; `false` otherwise.
     *          The result is `false` if `toffset` is
     *          negative or greater than the length of this
     *          `$string`; otherwise the result is the same
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
    public static function startsWith($string, $prefix, $offset = 0)
    {
        return false;
    }

    /**
     * Tests if this string ends with the specified suffix.
     *
     * @param string $string
     * @param string $suffix
     * @return bool
     */
    public static function endsWith($string, $suffix)
    {
        return false;
    }

    /**
     * Converts all of the characters in `$string` to lower
     * case using the rules of the default locale.
     * --RU--
     * Конвертирует все символы строки в нижний регистр и возвращает её.
     *
     * @param string $string
     * @return string
     */
    public static function lower($string)
    {
        return '';
    }

    /**
     * Converts all of the characters in ``$string`` to upper
     * case using the rules of the default locale.
     * --RU--
     * Конвертирует все символы строки в верхний регистр и возвращает её.
     *
     * @param string $string
     * @return string
     */
    public static function upper($string)
    {
        return '';
    }

    /**
     * Returns the length of ``$string``.
     * The length is equal to the number of `Unicode code units` in the string.
     * --RU--
     * Возвращает длину строки.
     * Длина строки равна количеству юникодных единиц в строке.
     *
     * @param string $string
     * @return int
     */
    public static function length($string)
    {
        return 0;
    }

    /**
     * Replaces each substring of this string that matches the literal target
     * sequence with the specified literal replacement sequence. The
     * replacement proceeds from the beginning of the string to the end, for
     * example, replacing "aa" with "b" in the string "aaa" will result in
     * "ba" rather than "ab".
     * --RU--
     * Заменяет каждую подстроку на другую.
     *
     * @param string $string
     * @param string $target The sequence of char values to be replaced
     * @param string $replacement The replacement sequence of char values
     * @return string
     */
    public static function replace($string, $target, $replacement)
    {
        return '';
    }

    /**
     * Return s a new string consisting of the original ``$string`` repeated
     * --RU--
     * Возвращает новую строку состояющую из повторов указанной строки.
     *
     * @param string $string
     * @param int $amount number of times to repeat str
     * @return string
     */
    public static function repeat($string, $amount)
    {
        return '';
    }

    /**
     * Returns a copy of the string, with leading and trailing whitespace
     * omitted.
     * --RU--
     * Возвращает копию строки без пробельных символов слева и справа.
     *
     * @param string $string
     * @param string $charList
     * @return string
     */
    public static function trim($string, $charList = "\t\n\r\v\0 ")
    {
        return '';
    }

    /**
     * @param string $string
     * @param string $charList
     * @return string
     */
    public static function trimRight($string, $charList = "\t\n\r\v\0 ")
    {
        return '';
    }

    /**
     * @param string $string
     * @param string $charList
     * @return string
     */
    public static function trimLeft($string, $charList = "\t\n\r\v\0 ")
    {
        return '';
    }

    /**
     * Reverse the string.
     * --RU--
     * Возвращает перевернутую строку.
     *
     * @param string $string
     * @return string
     */
    public static function reverse($string)
    {
        return '';
    }

    /**
     * Returns a randomized string based on chars in $string
     * @param string $string
     * @return string
     */
    public static function shuffle($string)
    {
        return '';
    }

    /**
     * Returns a new random string based on set.
     * --RU--
     * Возвращает новую случайную строку основанную на наборе символов из $set.
     *
     * @param int $length
     * @param string $set
     * @return string
     */
    public static function random($length = 16, $set = 'qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789')
    {
        return '';
    }

    /**
     * The method like explode() in Zend PHP.
     * --RU--
     * Разбивает строку на массив используя строку-разделитель $separator.
     * Аналог функции explode() из zend php.
     *
     * @param string $string
     * @param string $separator
     * @param int $limit
     * @return array
     */
    public static function split($string, $separator, $limit = 0)
    {
        return [];
    }

    /**
     * The method like ``implode()`` in Zend PHP.
     * --RU--
     * Собирает массив в строку с разделителем $separator.
     * Аналог функции implode() из zend php.
     *
     * @param array|\Iterator $iterable
     * @param string $separator
     * @param int $limit
     * @return string
     */
    public static function join($iterable, $separator, $limit = 0)
    {
        return '';
    }

    /**
     * Converts $string by using $charset and returns a binary string.
     * --RU--
     * Кодирует юникодную строку в любую другую кодировку и возвращает бинарную строку.
     *
     * @param string $string
     * @param string $charset e.g. UTF-8, Windows-1251, etc.
     * @return string binary string
     */
    public static function encode($string, $charset)
    {
        return '';
    }

    /**
     * Decodes $string by using $charset to UNICODE, returns a unicode string.
     * --RU--
     * Декодирует строку из указанной кодировке в родную юникодную строку.
     *
     * @param string $string
     * @param string $charset e.g. UTF-8, Windows-1251, etc.
     * @return string binary string
     */
    public static function decode($string, $charset)
    {
        return '';
    }

    /**
     * Returns true if $string is integer number (e.g: '12893', '3784', '0047')
     *
     *  - for ``123`` - true
     *  - for ``00304`` - true
     *  - for ``3389e4`` - false
     *  - for ``3.49`` - false
     *  - for ``23  `` - false
     *
     * @param string $string
     * @param bool $bigNumbers
     * @return bool
     */
    public static function isNumber($string, $bigNumbers = true)
    {
        return false;
    }

    /**
     * @param string $string
     * @return bool
     */
    public static function isLower($string)
    {
        return false;
    }

    /**
     * @param $string
     * @return bool
     */
    public static function isUpper($string)
    {
        return false;
    }

    /**
     * @param string $string
     * @return string
     */
    public static function lowerFirst($string)
    {
    }

    /**
     * @param string $string
     * @return string
     */
    public static function upperFirst($string)
    {
    }

    /**
     * Parse text as json, ini, yaml, etc.
     *
     * @param string $string
     * @param string $format like json, yaml
     * @param int $flags
     * @return mixed
     * @throws ProcessorException
     */
    public static function parseAs(string $string, string $format, $flags = -1)
    {
    }

    /**
     * Format value as ini, json, yml, etc.
     *
     * @param mixed $input
     * @param string $format like json, yaml
     * @param int $flags
     * @return string
     * @throws ProcessorException
     */
    public static function formatAs($input, string $format, $flags = -1): string
    {
    }

    /**
     * @param string $string
     * @param array ...$args
     * @return string
     */
    public static function format($string, ...$args)
    {
    }

    /**
     * Returns true if string contains a substring.
     * --RU--
     * Возвращает true если строка содержит укзанную подстроку.
     * @param string $string
     * @param string $search
     * @return bool
     */
    public static function contains($string, $search)
    {
    }

    /**
     * --RU--
     * Возвращает количество найденный строк в другой строке.
     * @param string $string
     * @param string $subString
     * @param int $offset
     * @return int
     */
    public static function count($string, $subString, $offset = 0)
    {
    }

    /**
     * @param null|string $value
     * @return string uuid of $value if it is not null, else random uuid
     */
    public static function uuid($value = null)
    {
    }

    /**
     * --RU--
     * Возвращает хеш строки.
     * @param string $string
     * @param string $algorithm MD5, SHA-1, SHA-256, etc.
     * @return string
     * @throws \Exception if the algorithm is not supported
     */
    public static function hash($string, $algorithm = 'SHA-1')
    {
    }

    /**
     * --RU--
     * Переводит многострочный текст в массив, если $removeEmpty true, то удаляет пустые строки из результата.
     * @param string $string
     * @param bool $removeEmpty
     * @return array
     */
    public static function lines($string, $removeEmpty = false)
    {
    }
}
