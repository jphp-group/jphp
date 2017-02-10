<?php
namespace php\lang;

/**
 * Class SourceMap
 * @packages std, core
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
     * @return int -1 if not found
     */
    public function getSourceLine($compiledLine)
    {
    }

    /**
     * @param int $sourceLine
     * @return int -1 if not found
     */
    public function getCompiledLine($sourceLine)
    {
    }

    /**
     * @param array $inserts int[][2] [[line, lineCount], [line, lineCount], ...]
     * @param int $allCountLines original source line count.
     */
    public function insertLines(array $inserts, $allCountLines)
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