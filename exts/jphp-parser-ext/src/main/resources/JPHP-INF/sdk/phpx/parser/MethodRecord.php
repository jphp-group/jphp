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
}