<?php
namespace php\lang;

/**
 * Class ClassLoader
 * @package php\lang
 */
abstract class ClassLoader
{
    const __PACKAGE__ = 'std, core';

    /**
     * @param string $name
     */
    abstract public function loadClass($name);

    public function register()
    {
    }

    public function unregister()
    {
    }
}