<?php
namespace php\format;

use php\io\Stream;

/**
 * Class YamlProcessor
 * @package php\format
 *
 * @packages std, yaml
 */
class YamlProcessor extends Processor
{
    const SERIALIZE_PRETTY_FLOW = 1;
    const SERIALIZE_CANONICAL = 2;
    const SERIALIZE_EXPLICIT_START = 4;
    const SERIALIZE_EXPLICIT_END = 8;
    const SERIALIZE_NOT_SPLIT_LINES = 16;

    /**
     * @param int $flags
     */
    public function __construct($flags = 0) { }

    public function format($value)
    {
    }

    public function formatTo($value, Stream $output)
    {
    }

    public function parse($source)
    {
    }
}