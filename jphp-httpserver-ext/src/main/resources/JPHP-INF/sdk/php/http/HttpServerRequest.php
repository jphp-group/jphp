<?php
namespace php\http;

/**
 * Class HttpServerRequest
 * @package php\http
 */
class HttpServerRequest
{
    protected function __construct()
    {
    }

    /**
     * Stop handle requests.
     */
    public function end()
    {
    }

    /**
     * @param $name
     * @return string
     */
    function header($name)
    {
    }

    /**
     * @param $name
     * @return string
     */
    function param($name)
    {
    }

    /**
     * @return string
     */
    function query()
    {
    }

    /**
     * @return string
     */
    function path()
    {
    }

    /**
     * @return string
     */
    function method()
    {
    }

    /**
     * @return string
     */
    function sessionId()
    {
    }
}