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
class items {

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
}
