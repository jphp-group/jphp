<?php
namespace php\util;

use php\lang\ClassLoader;

/**
 * Class LauncherClassLoader
 * @package php\util
 */
class LauncherClassLoader extends ClassLoader
{
    const __PACKAGE__ = 'std, core';

    /**
     * @param string $name
     */
    public function loadClass($name)
    {
        // native.
    }
}