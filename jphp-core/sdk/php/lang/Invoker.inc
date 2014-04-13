<?php
namespace php\lang;

/**
 * Class Invoker
 * @package php\lang
 */
class Invoker {

    /**
     * @param callable $callback
     */
    public function __construct(callable $callback) {  }

    /**
     * @param array $args
     * @return mixed
     * @return int
     */
    public function callArray(array $args) { return 0; }

    /**
     * @param ...
     * @return int
     */
    public function call() { return 0; }

    /**
     * alias of call() method
     */
    public function __invoke() {  }

    /**
     * @return bool
     */
    public function canAccess() { return false; }

    /**
     * @return string
     */
    public function getDescription() { return ''; }

    /**
     * @return int
     */
    public function getArgumentCount() { return 0; }

    /**
     * @return bool
     */
    public function isClosure() { return false; }

    /**
     * @return bool
     */
    public function isNamedFunction() { return false; }

    /**
     * @return bool
     */
    public function isStaticCall() { return false; }

    /**
     * @return bool
     */
    public function isDynamicCall() { return false; }

    /**
     * @param mixed $callback
     * @return Invoker|null - returns null if passed is not callable
     */
    public static function of($callback) { return new Invoker($callback); }
}