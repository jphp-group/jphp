<?php
namespace php\format;
use php\io\Stream;

/**
 * Class JsonProcessor
 * @package php\format
 */
class JsonProcessor extends Processor {

    const SERIALIZE_PRETTY_PRINT = 1;
    const DESERIALIZE_AS_ARRAYS  = 1024;

    /**
     * @param int $flags
     */
    public function __construct($flags = 0) { }

    /**
     * @param string|Stream $json
     * @return mixed
     */
    public function parse($json) { return []; }

    /**
     * @param mixed $value
     * @return string
     */
    public function format($value) { return ''; }

    /**
     * @param mixed $value
     * @param Stream $output
     */
    public function formatTo($value, Stream $output) { }

    /**
     * @param string $nameOfType - null, int, float, string, bool, object, array
     * @param callable $handler (mixed $value) -> mixed
     */
    public function onSerialize($nameOfType, callable $handler = null) { }

    /**
     * @param string $className
     * @param callable $handler
     */
    public function onClassSerialize($className, callable $handler = null) { }
}
