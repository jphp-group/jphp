<?php
namespace php\format;
use php\io\Stream;

/**
 * Class Processor
 * @package php\format
 */
abstract class Processor {

    abstract public function format($value);
    abstract public function formatTo($value, Stream $output);
    abstract public function parse($string);
}
