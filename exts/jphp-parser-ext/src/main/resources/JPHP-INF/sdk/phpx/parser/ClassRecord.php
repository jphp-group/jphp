<?php
namespace phpx\parser;

/**
 * Class ClassRecord
 * @package phpx\parser
 */
class ClassRecord extends AbstractSourceRecord
{
    /**
     * @var NamespaceRecord
     */
    public $namespaceRecord;

    /**
     * @var ClassRecord
     */
    public $parent;

    /**
     * @var string CLASS, INTERFACE, TRAIT
     */
    public $type = 'CLASS';

    /**
     * @var bool
     */
    public $shortParentName = false;

    /**
     * @param ClassRecord $record
     */
    public function addInterface(ClassRecord $record)
    {
    }

    /**
     * @param MethodRecord $method
     */
    public function addMethod(MethodRecord $method)
    {
    }

    /**
     * @param $name
     * @return MethodRecord
     */
    public function getMethod($name)
    {
    }

    /**
     * @param $name
     */
    public function removeMethod($name)
    {
    }

    /**
     * @return MethodRecord[]
     */
    public function getMethods()
    {
    }
}