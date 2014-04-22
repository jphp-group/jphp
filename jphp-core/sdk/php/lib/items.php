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
     * Iterates all elements of $collection and applies the $callback to each element
     * --RU--
     * Проходится по всем элементам $collection и применяет $callback к каждому
     *
     * @param array|Iterator $collection
     * @param callable $callback -> ($value, $key): bool
     *          to break iteration, return *FALSE* (strongly) from $callback
     *          --RU-- чтобы прервать итерацию верните ``false`` (строго) из $callback
     * @return int - iteration count --RU-- количество итераций
     */
    public static function each($collection, callable $callback) { return 0; }

    /**
     * Iterates all elements of $collection with slice $size
     * --RU--
     * Проходится по всем элементам $collection с учетом среза $size
     *
     * @param array|Iterator $collection
     * @param int $size slice size --RU-- размер среза
     * @param callable $callback -> (array $slice): bool,
     *  to break iteration, return ``FALSE`` (strongly) from $callback
     * @return int iteration slice count --RU-- количество разделенных итераций
     */
    public static function eachSlice($collection, $size, callable $callback) { return 0; }

    /**
     * Finds the first value matching the $callback condition
     * --RU--
     * Находит первый элемент, значение которого удовлетворило условию $callback
     *
     * @param array|Iterator $collection
     * @param callable|null $callback -> ($value, $key): bool
     * @return mixed|null - null if not found
     */
    public static function find($collection, callable $callback = null) { return []; }

    /**
     * Finds the all values matching the $callback condition.
     * --RU--
     * Находит все элементы, значения которых было успешно профильтрованы с помощь $callback
     *
     * @param array|Iterator $collection
     * @param callable $callback
     * @return array
     */
    public static function findAll($collection, callable $callback = null) { return []; }

    /**
     * Returns the first element(s) of $collection
     * --RU--
     * Возвращает первый элемент(ы) коллекции
     *
     * @param array|Iterator $collection
     * @param int $count
     * @return array|mixed returns non-array (one element) if passed $count <= 1
     */
    public static function first($collection, $count = 1) { return []; }

    /**
     * @param array|Iterator $collection
     * @param callable $callback -> ($value, $key): mixed
     * @return array
     */
    public static function map($collection, callable $callback) { return []; }

    /**
     * Combines all elements of $collection by applying a binary operation, specified by a callback
     * --RU--
     * Сомбинирует все элементы коллекции с применением определенной операции $callback
     *
     * @param array|Iterator $collection
     * @param callable $callback
     * @return mixed|null
     */
    public static function reduce($collection, callable $callback) { }

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
