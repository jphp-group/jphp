<?php
namespace php\xml;

use php\format\Processor;
use php\format\ProcessorException;
use php\io\Stream;

/**
 * Class XmlProcessor
 * @package php\xml
 */
class XmlProcessor extends Processor
{
    /**
     * @param DomDocument $value
     * @return string xml
     * @throws ProcessorException
     */
    public function format($value) { }

    /**
     * @param DomDocument $value
     * @param Stream $output
     * @throws ProcessorException
     */
    public function formatTo($value, Stream $output) { }

    /**
     * @param Stream|string $string  stream of string of xml
     * @return DomDocument
     * @throws ProcessorException
     */
    public function parse($string)
    {
    }

    /**
     * @return DomDocument
     */
    public function createDocument()
    {
    }
}