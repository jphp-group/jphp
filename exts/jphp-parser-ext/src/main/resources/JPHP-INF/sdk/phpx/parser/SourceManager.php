<?php
namespace phpx\parser;
use php\lang\IllegalArgumentException;

/**
 * Class SourceManager
 * @package phpx\parser
 */
class SourceManager
{
    /**
     * @param callable $filter (ModuleRecord $module)
     * @return mixed
     */
    public function find(callable $filter)
    {
    }

    /**
     * @param callable $filter (ModuleRecord $module) -> array or mixed
     * @return mixed
     */
    public function findAll(callable $filter)
    {
    }

    /**
     * @param SourceFile $any
     */
    public function update($any)
    {
    }

    /**
     * @param SourceFile $file
     * @param SourceWriter $writer
     */
    public function write(SourceFile $file, SourceWriter $writer)
    {
    }

    /**
     * @param $path
     * @param $uniqueId
     * @return SourceFile
     */
    public function get($path, $uniqueId)
    {
    }
}