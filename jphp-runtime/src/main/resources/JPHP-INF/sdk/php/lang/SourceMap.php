<?php
namespace php\lang;

/**
 * Class SourceMap
 * @package php\lang
 */
class SourceMap
{
    /**
     * @param string $moduleName
     */
    public function __construct($moduleName)
    {
    }

    /**
     * @return string
     */
    public function getModuleName()
    {
    }

    /**
     * @param int $compiledLine
     * @return int
     */
    public function getSourceLine($compiledLine)
    {
    }

    /**
     * @param int $sourceLine
     * @param int $compiledLine
     */
    public function addLine($sourceLine, $compiledLine)
    {
    }

    /**
     * Remove all lines.
     */
    public function clear()
    {
    }

    /**
     * @return array
     */
    public function toArray()
    {
    }
}