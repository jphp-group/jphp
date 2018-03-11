<?php
namespace php\format;
use php\io\Stream;

/**
 * Class JsonProcessor
 * @package php\format
 *
 * @packages std, core
 */
class JsonProcessor extends Processor
{
    const SERIALIZE_PRETTY_PRINT = 1;
    const DESERIALIZE_AS_ARRAYS  = 1024;
    const DESERIALIZE_LENIENT    = 2048;

    /**
     * @param int $flags
     */
    public function __construct($flags = 0) { }

    /**
     * @param string|Stream $json
     * @return mixed
     * @throws ProcessorException
     */
    public function parse($json) { return []; }

    /**
     * @param mixed $value
     * @return string
     * @throws ProcessorException
     */
    public function format($value) { return ''; }

    /**
     * @param mixed $value
     * @param Stream $output
     * @throws ProcessorException
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
