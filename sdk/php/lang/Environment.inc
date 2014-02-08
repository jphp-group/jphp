<?php
namespace php\lang;

/**
 * Class Environment
 * @package php\lang
 */
class Environment {
    /**
     * @param Environment $parent
     */
    public function __construct(Environment $parent = NULL){ }

    /**
     * @param callable $runnable - in new environment
     * @return mixed
     */
    public function execute(callable $runnable) { }

    /**
     * @param string $className
     * @throws \Exception - if class not found or already registered
     */
    public function importClass($className) { }

    /**
     * @param string $className
     * @throws \Exception - if class not found or already registered
     */
    public function exportClass($className) { }

    /**
     * @param string $functionName
     * @throws \Exception - if function not found or already registered
     */
    public function importFunction($functionName) { }

    /**
     * @param string $functionName
     * @throws \Exception - if function not found or already registered
     */
    public function exportFunction($functionName) { }

    /**
     * @param string $name
     * @param mixed $value - scalar value
     * @param bool $caseSensitive
     * @throws \Exception - if constant already registered or value is not scalar type
     */
    public function defineConstant($name, $value, $caseSensitive = true) { }

    /**
     * @param callable $callback
     */
    public function onMessage(callable $callback) { }

    /**
     * @param mixed $message
     * @param ... args
     * @return mixed
     */
    public function sendMessage($message) { }

    /**
     * Get environment of current execution
     * @return Environment
     */
    public static function current() { }
}