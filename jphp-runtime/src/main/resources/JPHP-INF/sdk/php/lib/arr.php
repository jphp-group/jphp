<?php
namespace php\lib;

use ArrayAccess;
use Countable;
use Iterator;
use Traversable;

/**
 * Library for working with collections - arrays, iterators, etc.
 * --RU--
 * Библиотека для работы с коллекциями - массивы, итераторы и т.д.
 *
 * @packages std, core
 */
class arr
{


    private function __construct()
    {
    }

    /**
     * Returns element count of the collection
     *
     *   .. warning:: for iterators it will iterate all elements to return the result
     *
     * --RU--
     * Возвращает количество элементов коллекции
     *
     *   .. warning:: для итераторов для подсчета количества требуется итерация по всем элементам
     *
     * @param array|Countable|Iterator $collection
     * @return int element count --RU-- количество элементов
     */
    public static function count($collection)
    {
        return 0;
    }

    /**
     * @param array|Traversable $collection
     * @param mixed $value
     * @param bool $strict
     * @return bool
     */
    public static function has($collection, $value, $strict = false)
    {
        return false;
    }

    /**
     * Converts $collection to array
     * --RU--
     * Конвертирует коллекцию в массив
     *
     * @param array|\Traversable $collection
     * @param bool $withKeys
     * @return array
     */
    public static function toArray($collection, $withKeys = false)
    {
        return [];
    }

    /**
     * Alias of toArray()
     * @param array|Iterator $collection
     * @param bool|false $withKeys
     * @return array
     */
    public static function of($collection, $withKeys = false)
    {
        return [];
    }

    /**
     * Example: items::toList(['x' => 10, 20], 30, ['x' => 50, 60]) -> [10, 20, 30, 50, 60]
     *
     * @param $collection
     * @param ...
     * @return array
     */
    public static function toList($collection)
    {
        return [];
    }

    /**
     * Returns all keys of collection
     * --RU--
     * Возвращает все ключи коллекции
     *
     * @param array|Iterator $collection
     * @return array
     */
    public static function keys($collection)
    {
        return [];
    }

    /**
     * Returns all values of collection
     * --RU--
     * Возвращает все значения коллекции
     *
     * @param array|Iterator $collection
     * @return array
     */
    public static function values($collection)
    {
        return [];
    }

    /**
     * Combines two collections to array.
     *
     * @param array|Iterator $keys
     * @param array|Iterator $values
     * @return array|null returns null if size of arrays is not equals.
     */
    public static function combine($keys, $values)
    {
    }

    /**
     * @param array|Iterator $collection
     * @param $callback
     */
    public static function map($collection, callable $callback)
    {
    }

    /**
     * Returns a new array that is a one-dimensional flattening of this collection (recursively).
     * That is, for every element that is an collection, extract its elements into the new array.
     * If the optional $maxLevel argument > -1 the level of recursion to flatten.
     * --RU--
     * Возвращает новый массив полученный исходя из всех элементов коллекции рекурсивно.
     *
     * @param array|Iterator $collection
     * @param int $maxLevel
     * @return array
     */
    public static function flatten($collection, $maxLevel = -1)
    {
        return [];
    }

    /**
     * Sorts the specified list into ascending order
     *
     * @param array|Iterator $collection
     * @param callable $comparator ($o1, $o2) -> int where -1 smaller, 0 equal, 1 greater
     * @param bool $saveKeys
     * @return array
     */
    public static function sort($collection, callable $comparator = null, $saveKeys = false)
    {
        return [];
    }

    /**
     * Sorts the specified list into ascending order by keys
     *
     * @param array|Iterator $collection
     * @param callable $comparator ($key1, $key2)
     * @param bool $saveKeys
     * @return array
     */
    public static function sortByKeys($collection, callable $comparator = null, $saveKeys = false)
    {
        return [];
    }

    /**
     * Returns the last element of array.
     *
     * @param $array
     * @return mixed last value of array
     */
    public static function peak($array)
    {
    }

    /**
     * @param array|ArrayAccess $array
     * @param ...$values
     */
    public static function push(&$array, ...$values)
    {
    }

    /**
     * @param array $array
     * @return mixed
     */
    public static function pop(array &$array)
    {
    }

    /**
     * @param array $array
     * @return mixed
     */
    public static function shift(array &$array)
    {
    }

    /**
     * @param array $array
     * @param $values
     */
    public static function unshift(array &$array, ...$values)
    {
    }

    /**
     * @param Traversable|array $collection
     * @return mixed
     */
    public static function first($collection)
    {
    }

    /**
     * @param Traversable|array $collection
     * @return string|int|null
     */
    public static function firstKey($collection)
    {
    }

    /**
     * Alias to peek().
     * @param array $collection
     * @return mixed
     */
    public static function last($collection)
    {
    }

    /**
     * @param array $collection
     * @return string|int|null
     */
    public static function lastKey($collection)
    {
    }

    /**
     * @param array $array
     * @return array
     */
    public static function reverse(array $array)
    {
    }
}