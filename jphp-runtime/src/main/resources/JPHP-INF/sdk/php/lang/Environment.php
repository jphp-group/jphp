<?php
namespace php\lang;

/**
 * Class Environment
 * @package php\lang
 *
 * @packages std, core
 */
class Environment
{


    const CONCURRENT = 1; // experimental - for use in multi-threading
    const HOT_RELOAD = 2; // for hot-reload working (like classical PHP)

    /**
     * @param Environment $parent
     * @param int $flags Environment::HOT_RELOAD, Environment::CONCURRENT
     */
    public function __construct(Environment $parent = NULL, $flags = 0)
    {
    }

    /**
     * @param SourceMap $sourceMap
     */
    public function registerSourceMap(SourceMap $sourceMap)
    {
    }

    /**
     * @param SourceMap $sourceMap
     */
    public function unregisterSourceMap(SourceMap $sourceMap)
    {
    }

    /**
     * Executes $runnable in the environment
     * --RU--
     * Выполняет $runnable в текущем своем окружении
     *
     * @param callable $runnable - in new environment
     * @return mixed
     */
    public function execute(callable $runnable)
    {
    }

    /**
     * Imports the $className to the environment
     * --RU--
     * Импортирует класс в свое окружение
     *
     * @param string $className
     * @throws \Exception - if class not found or already registered
     */
    public function importClass($className)
    {
    }

    /**
     * Exports the $className from th environment
     * --RU--
     * Экмпортирует класс из своего окружения
     *
     * @param string $className
     * @throws \Exception - if class not found or already registered
     */
    public function exportClass($className)
    {
    }

    /**
     * Imports the $functionName to the environment
     * --RU--
     * Импортирует функцию в свое окружение
     *
     * @param string $functionName
     * @throws \Exception - if function not found or already registered
     *  --RU-- если функция не найдена или уже объявлена
     */
    public function importFunction($functionName)
    {
    }

    /**
     * Exports the $functionName from the environment
     * --RU--
     * Экспортирует функцию из своего окружения
     *
     * @param string $functionName
     * @throws \Exception - if function not found or already registered
     *  --RU-- если функция не найдена или уже объявлена
     */
    public function exportFunction($functionName)
    {
    }

    /**
     * Imports the all spl auto loaders to the environment.
     */
    public function importAutoLoaders()
    {
    }

    /**
     * @param string $name
     * @param mixed $value - scalar value
     * @param bool $caseSensitive
     * @throws \Exception - if constant already registered or value is not scalar type
     */
    public function defineConstant($name, $value, $caseSensitive = true)
    {
    }

    /**
     * Handles messages that sent to the environment
     * --RU--
     * Обрабатывает сообщения, что были посланы в окружение
     *
     * @param callable $callback
     */
    public function onMessage(callable $callback)
    {
    }

    /**
     * @param callable|null $callback
     */
    public function onOutput(callable $callback)
    {
    }

    /**
     * Send message to the environment
     * --RU--
     * Послать сообщение окружению
     *
     * @param mixed $message
     * @param ... args
     * @return mixed
     */
    public function sendMessage($message)
    {
    }

    /**
     * @param string $path
     * @return Module|null
     */
    public function findModule($path)
    {
    }

    /**
     * @return Package[]
     */
    public function getPackages()
    {
    }

    /**
     * @param string $name
     * @return bool
     */
    public function hasPackage($name)
    {
    }

    /**
     * @param string $name
     * @return Package
     */
    public function getPackage($name)
    {
    }

    /**
     * @param string $name
     * @param Package $package
     */
    public function setPackage($name, Package $package)
    {
    }

    /**
     * @param string $extensionId
     */
    public function registerExtension(string $extensionId)
    {
    }

    /**
     * Get environment of current execution
     * --RU--
     * Взять окружение текущего выполнения
     *
     * @return Environment
     */
    public static function current()
    {
    }

    /**
     * @param string $name
     * @param mixed $value
     * @throws \Exception if super-global already exists.
     */
    public function addSuperGlobal(string $name, $value = null)
    {
    }

    /**
     * @param string $name
     * @return bool
     */
    public function hasSuperGlobal(string $name): bool
    {
    }

    /**
     * @return array of string
     */
    public function getSuperGlobals(): array
    {
    }

    /**
     * $GLOBALS of environment.
     * @return array
     */
    public function getGlobals(): array
    {
    }

    /**
     * @param string $name
     * @return mixed
     */
    public function getGlobal(string $name)
    {
    }

    /**
     * @param string $name
     * @return bool
     */
    public function hasGlobal(string $name): bool
    {
    }

    /**
     * @param string $name
     * @param mixed $value
     */
    public function setGlobal(string $name, $value): void
    {
    }
}
