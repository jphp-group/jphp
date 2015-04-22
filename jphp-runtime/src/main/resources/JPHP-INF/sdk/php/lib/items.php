<?php
namespace php\lib;

use Countable;
use Iterator;

/**
 * Library for working with collections - arrays, iterators, etc.
 * --RU--
 * Библиотека для работы с коллекциями - массивы, итераторы и т.д.
 *
 * Class items
 * @package php\lib
 */
class Items {

    private function __construct() { }

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
    public static function count($collection) { return 0; }

    /**
     * Converts $collection to array
     * --RU--
     * Конвертирует коллекцию в массив
     *
     * @param array|Iterator $collection
     * @param bool $withKeys
     * @return array
     */
    public static function toArray($collection, $withKeys = false) { return []; }

    /**
     * Example: items::toList(['x' => 10, 20], 30, ['x' => 50, 60]) -> [10, 20, 30, 50, 60]
     *
     * @param $collection
     * @param ...
     * @return array
     */
    public static function toList($collection) { return []; }

    /**
     * Returns all keys of collection
     * --RU--
     * Возвращает все ключи коллекции
     *
     * @param array|Iterator $collection
     * @return array
     */
    public static function keys($collection) { return []; }

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
    public static function flatten($collection, $maxLevel = -1) { return []; }

    /**
     * Sorts the specified list into ascending order
     *
     * @param array|Iterator $collection
     * @param callable $comparator ($o1, $o2) -> int where -1 smaller, 0 equal, 1 greater
     * @param bool $saveKeys
     * @return array
     */
    public static function sort($collection, callable $comparator = null, $saveKeys = false) { return []; }

    /**
     * Sorts the specified list into ascending order by keys
     *
     * @param array|Iterator $collection
     * @param callable $comparator ($key1, $key2)
     * @param bool $saveKeys
     * @return array
     */
    public static function sortByKeys($collection, callable $comparator = null, $saveKeys = false) { return []; }
}
