<?php
namespace packager\repository;

use php\lib\str;

/**
 * Class SingleExternalRepository
 * @package packager\repository
 */
abstract class SingleExternalRepository extends ExternalRepository
{
    abstract public function getHash(): string;
}