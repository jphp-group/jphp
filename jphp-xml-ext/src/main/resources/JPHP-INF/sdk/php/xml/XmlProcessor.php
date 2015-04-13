<?php
namespace php\xml;

use php\format\Processor;
use php\io\Stream;

class XmlProcessor extends Processor {

    /**
     * @param DomDocument $value
     * @return string xml
     */
    public function format($value) { }

    /**
     * @param DomDocument $value
     * @param Stream $output
     */
    public function formatTo($value, Stream $output) { }

    /**
     * @param Stream|string $string  stream of string of xml
     * @return DomDocument
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