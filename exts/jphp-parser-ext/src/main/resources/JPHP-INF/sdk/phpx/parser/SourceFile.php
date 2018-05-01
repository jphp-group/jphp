<?php
namespace phpx\parser;
use php\io\Stream;

/**
 * Class SourceFile
 * @package phpx\parser
 */
class SourceFile
{
    /**
     * @var ModuleRecord
     */
    public $moduleRecord;

    /**
     * @param string|Stream $path
     * @param string $uniqueId
     */
    public function __construct($path, $uniqueId)
    {
    }

    /**
     * @return bool
     */
    public function isReadOnly()
    {
    }

    /**
     * Returns full name with namespace for class, func and constant name.
     *
     * @param string $name
     * @param NamespaceRecord|SourceToken $namespace
     * @param string $useType CLASS, FUNCTION, CONSTANT
     * @return string
     */
    public function fetchFullName(string $name, NamespaceRecord $namespace, string $useType): string
    {
    }

    /**
     * @param SourceManager $manager
     */
    public function update(SourceManager $manager)
    {
    }
}