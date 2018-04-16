<?php

use php\util\Flow;

const JPHP_VERSION = 'x.x.x';

/**
 * @param iterable[] $others
 * @return Flow
 */
function flow(iterable... $others): Flow
{
}

/**
 * @param string $name
 * @param array $classes
 * @param array $functions
 * @param array $constants
 * @return bool
 */
function define_package($name, array $classes, array $functions = [], array $constants = [])
{
}

/**
 * @param string $name
 * @param string $file
 */
function define_autoload_package($name, $file)
{
}