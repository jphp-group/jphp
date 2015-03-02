<?php
namespace php\framework\loader;
use Traversable;

/**
 * Class StandardClassLoader
 * @package php\framework
 */
class StandardClassLoader extends ClassLoader
{
    /**
     * @param string $prefix
     * @param string|array|Traversable $paths
     */
    public function addPrefix($prefix, $paths)
    {

    }

    /**
     * @param array|Traversable $prefixes
     */
    public function addPrefixes($prefixes)
    {

    }

    /**
     * {@inheritdoc}
     */
    function loadClass($className)
    {
    }
}