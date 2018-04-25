<?php
namespace phpx\parser;

/**
 * Class PropertyRecord
 * @package phpx\parser
 */
class PropertyRecord extends AbstractSourceRecord
{
    /**
     * @var string
     */
    public $name;

    /**
     * @var string
     */
    public $classRecord;

    /**
     * @var bool
     */
    public $static;
}