<?php
namespace php\format;

/**
 * Class Processor
 * @package php\format
 */
abstract class Processor {

    abstract public function format($value);
    abstract public function parse($string);
}
