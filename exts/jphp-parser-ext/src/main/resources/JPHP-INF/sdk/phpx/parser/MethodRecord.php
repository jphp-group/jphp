<?php
namespace phpx\parser;

/**
 * Class MethodRecord
 * @package phpx\parser
 */
class MethodRecord extends AbstractSourceRecord
{
    /**
     * @var NamespaceRecord
     */
    public $namespaceRecord;

    /**
     * @var NamespaceRecord
     */
    public $classRecord;

    /**
     * @var bool
     */
    public $static = false;

    /**
     * @var bool
     */
    public $final = false;

    /**
     * @var bool
     */
    public $abstract = false;

    /**
     * @var string
     */
    public $returnTypeHint = 'VOID';

    /**
     * @var string
     */
    public $returnTypeHintClass;

    /**
     * @var ArgumentRecord[]
     */
    public $argumentRecords = [];
}