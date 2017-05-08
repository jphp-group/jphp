<?php
namespace php\http;

/**
 * Class HttpServerResponse
 * @package php\http
 */
class HttpServerResponse
{
    protected function __construct()
    {
    }

    /**
     * @param int $status
     * @param string $message
     * @return HttpServerResponse
     */
    public function status($status, $message = null)
    {
    }

    /**
     * @param string $value
     * @return HttpServerResponse
     */
    public function contentType($value)
    {
    }

    /**
     * @param int $value
     * @return HttpServerResponse
     */
    public function contentLength($value)
    {
    }

    /**
     * @param mixed $content
     * @param string $charset
     * @return HttpServerResponse
     */
    public function write($content, $charset = 'UTF-8')
    {
    }

    /**
     * @param string $name
     * @param string $value
     * @return HttpServerResponse
     */
    public function header($name, $value)
    {
    }

    /**
     * @param string $location
     * @return string
     * @return HttpServerResponse
     */
    public function redirect($location)
    {
    }

    /**
     * Forces any content in the buffer to be written to the client.  A call
     * to this method automatically commits the response, meaning the status
     * code and headers will be written.
     * @return HttpServerResponse
     */
    public function flush()
    {
    }
}