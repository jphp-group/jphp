<?php
namespace php\util;

use php\lang\ClassLoader;

/**
 * Class LauncherClassLoader
 * @package php\util
 * @packages std, core
 */
class LauncherClassLoader extends ClassLoader
{


    /**
     * @param string $name
     */
    public function loadClass($name)
    {
        // native.
    }
}