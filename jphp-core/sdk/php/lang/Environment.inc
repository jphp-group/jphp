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
     * @param int $flags Environment::HOT_RELOAD, Environment::CONCURRENT
     */
    public function __construct(Environment $parent = NULL, $flags = 0){ }

    /**
     * Executes $runnable in the environment
     * --RU--
     * Выполняет $runnable в текущем своем окружении
     *
     * @param callable $runnable - in new environment
     * @return mixed
     */
    public function execute(callable $runnable) { }

    /**
     * Imports the $className to the environment
     * --RU--
     * Импортирует класс в свое окружение
     *
     * @param string $className
     * @throws \Exception - if class not found or already registered
     */
    public function importClass($className) { }

    /**
     * Exports the $className from th environment
     * --RU--
     * Экмпортирует класс из своего окружения
     *
     * @param string $className
     * @throws \Exception - if class not found or already registered
     */
    public function exportClass($className) { }

    /**
     * Imports the $functionName to the environment
     * --RU--
     * Импортирует функцию в свое окружение
     *
     * @param string $functionName
     * @throws \Exception - if function not found or already registered
     *  --RU-- если функция не найдена или уже объявлена
     */
    public function importFunction($functionName) { }

    /**
     * Exports the $functionName from the environment
     * --RU--
     * Экспортирует функцию из своего окружения
     *
     * @param string $functionName
     * @throws \Exception - if function not found or already registered
     *  --RU-- если функция не найдена или уже объявлена
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
     * Handles messages that sent to the environment
     * --RU--
     * Обрабатывает сообщения, что были посланы в окружение
     *
     * @param callable $callback
     */
    public function onMessage(callable $callback) { }

    /**
     * Send message to the environment
     * --RU--
     * Послать сообщение окружению
     *
     * @param mixed $message
     * @param ... args
     * @return mixed
     */
    public function sendMessage($message) { }

    /**
     * Get environment of current execution
     * --RU--
     * Взять окружение текущего выполнения
     *
     * @return Environment
     */
    public static function current() { }
}
