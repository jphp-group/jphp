<?php

use php\util\Flow;

const JPHP_VERSION = 'x.x.x';

/**
 * @param Traversable|array $iterator
 * @return Flow
 */
function flow($iterator)
{
    return Flow::of($iterator);
}