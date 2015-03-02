<?php
namespace php\framework\loader;
use php\framework\exception\ClassLoaderException;

/**
 * Class ClassLoader
 * @package php\framework
 */
abstract class ClassLoader
{
    /**
     * @param $className
     */
    abstract function loadClass($className);

    /**
     * @param bool $prepend
     * @throws ClassLoaderException if classLoader is already registered
     */
    public function register($prepend = false) {}

    /**
     * @throws ClassLoaderException if classLoader is not registered
     */
    public function unregister() {}
}