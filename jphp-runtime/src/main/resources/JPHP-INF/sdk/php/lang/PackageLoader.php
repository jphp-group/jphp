<?php
namespace php\lang;

/** *
 * @packages std, core
 */
abstract class PackageLoader
{
    /**
     * @param string $name
     * @return Package|null
     */
    abstract public function load($name);

    /**
     * Register package in current env.
     */
    public function register()
    {
    }

    /**
     * Unregister package in current env.
     */
    public function unregister()
    {
    }
}