<?php
namespace phpx\parser;

/**
 * Class NamespaceRecord
 * @package phpx\parser
 */
class NamespaceRecord extends AbstractSourceRecord
{
    /**
     * @return bool
     */
    public function isGlobally() {}

    /**
     * @param UseImportRecord $use
     */
    public function addUseImport(UseImportRecord $use) {}

    /**
     * @param MethodRecord $function
     */
    public function addFunction(MethodRecord $function) {}

    /**
     * @param ClassRecord $class
     */
    public function addClass(ClassRecord $class) {}
}