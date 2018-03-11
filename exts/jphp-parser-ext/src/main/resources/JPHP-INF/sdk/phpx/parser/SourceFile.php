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
}