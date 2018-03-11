<?php
namespace phpx\parser;

/**
 * Class AbstractSourceRecord
 * @package phpx\parser
 */
abstract class AbstractSourceRecord
{
    /**
     * @var string
     */
    public $name;

    /**
     * @readonly
     * @var string
     */
    public $idName;

    /**
     * @var string
     */
    public $shortName;

    /**
     * @var string
     */
    public $namespace;

    /**
     * Remove all data.
     */
    public function clear() {}
}