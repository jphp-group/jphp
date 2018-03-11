<?php
namespace phpx\parser;
use php\io\Stream;

/**
 * Class SourceTokenizer
 * @package phpx\parser
 */
class SourceTokenizer
{
    /**
     * @param string|Stream $source
     * @param string $name
     * @param string $charset e.g.: UTF-8
     */
    public function __construct($source, $name, $charset)
    {
    }

    /**
     * @return SourceToken
     */
    public function next()
    {
    }

    /**
     * @return SourceToken[]
     */
    public function fetchAll()
    {
    }

    public function close()
    {
    }

    /**
     * @param $string
     * @return SourceToken[]
     */
    public static function parseAll($string)
    {
    }
}