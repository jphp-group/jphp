<?php
namespace php\lang;

/**
 * Class Environment
 * @package php\lang
 */
class Environment {
    const CONCURRENT = 1; // experimental - for use in multi-threading
    const HOT_RELOAD = 2; // for hot-reload working (like classical PHP)

    /**
     * @param Environment $parent
     * @param int $flags - HOT_RELOAD, CONCURRENT
     */
    public function __construct(Environment $parent = NULL, $flags = 0){ }

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
