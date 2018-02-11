<?php
namespace php\format;
use php\io\Stream;

/**
 * Class Processor
 * @package php\format
 *
 * @packages std, core
 */
abstract class Processor
{
    /**
     * @param $value
     * @return string
     */
    abstract public function format($value);

    /**
     * @param $value
     * @param Stream $output
     * @return mixed
     */
    abstract public function formatTo($value, Stream $output);

    /**
     * @param string|Stream $source
     * @return mixed
     */
    abstract public function parse($source);

    /**
     * @param string $processorClass
     * @param string $code
     */
    public static function register(string $code, string $processorClass)
    {
    }

    /**
     * @param string $code
     * @return bool
     */
    public static function unregister(string $code): bool
    {
    }

    /**
     * @param string $code
     * @return bool
     */
    public static function isRegistered(string $code): bool
    {
    }
}
