<?php
namespace php\lang;

/**
 * Class for calling methods/functions/etc.
 * --RU--
 * Класс для вызова методов/функций/и т.д.
 *
 * @packages std, core
 */
class Invoker
{


    /**
     * @param callable $callback
     */
    public function __construct(callable $callback) {  }

    /**
     * Call with array arguments
     * --RU--
     * Вызвать с массивом аргументов
     *
     * @param array $args
     * @return mixed
     * @return int|mixed
     */
    public function callArray(array $args) { return 0; }

    /**
     * Call the current callback
     * --RU--
     * Вызвать текущий callback
     *
     * @param ...
     * @return int|mixed
     */
    public function call() { return 0; }

    /**
     * Alias of call() method
     * --RU--
     * Синоним метода call()
     */
    public function __invoke() {  }

    /**
     * Check access to invoke the method at a moment
     * --RU--
     * Проверить - есть ли доступ для вызова метода в какой-то момент
     * @return bool
     */
    public function canAccess() { return false; }

    /**
     * Returns description of the method - name + argument info
     * --RU--
     * Возвращает описание метода - название + информацию об аргументах
     *
     * @return string
     */
    public function getDescription() { return ''; }

    /**
     * Returns argument count of the method
     * --RU--
     * Возвращает количество аргументов текущего метода
     *
     * @return int
     */
    public function getArgumentCount() { return 0; }

    /**
     * Checks it is a closure
     * --RU--
     * Проверяет - является ли метод замыканием
     *
     * @return bool
     */
    public function isClosure() { return false; }

    /**
     * Checks it is a named function
     * --RU--
     * Проверяет - является ли это именованной функцией
     *
     * @return bool
     */
    public function isNamedFunction() { return false; }

    /**
     * Checks it is a static call
     * --RU--
     * Проверяет - является ли это статичным вызовом
     *
     * @return bool
     */
    public function isStaticCall() { return false; }

    /**
     * Checks it is a dynamic call
     * --RU--
     * Проверяет - является ли это динамичным вызовом
     *
     * @return bool
     */
    public function isDynamicCall() { return false; }

    /**
     * @param mixed|callable $callback
     * @return Invoker|null - returns ``null`` if passed is not callable
     *   --RU-- возвращает ``null`` если передан не валидный $callback
     */
    public static function of($callback) { return new Invoker($callback); }
}
