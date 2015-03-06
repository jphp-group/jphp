<?php
namespace php\framework\web;
use php\io\Stream;

/**
 * Class HttpPart
 * @package php\framework\web
 */
abstract class HttpPart
{
    /** @var Stream */
    public $input;

    /** @var string */
    public $name;

    /** @var string */
    public $contentType;

    /** @var int */
    public $size;

    /**
     * @param string $name
     * @return string
     */
    public function getHeader($name) {}

    /**
     * @param string $name
     * @return string[]
     */
    public function getHeaders($name) {}

    /**
     * @return string[]
     */
    public function getHeaderNames() {}
}