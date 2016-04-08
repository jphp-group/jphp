<?php
namespace php\net;
use php\io\Stream;

/**
 * Class HttpResponse
 * @package php\net
 */
class HttpResponse extends HttpMessage
{
    /**
     * @return Stream
     */
    public function getBodyStream()
    {
    }

    /**
     * @param Stream $stream
     */
    public function setBodyStream(Stream $stream)
    {
    }

    /**
     * @return int
     */
    public function getStatusCode()
    {
    }

    /**
     * @param int $statusCode
     */
    public function setStatusCode($statusCode)
    {
    }

    /**
     * @return string
     */
    public function getStatusMessage()
    {
    }

    /**
     * @param string $statusMessage
     */
    public function setStatusMessage($statusMessage)
    {
    }

    /**
     * @return array[] [name, value, maxAge, domain, path, secure, httpOnly, comment]
     */
    public function getRawCookies()
    {
    }

    /**
     * @param array $cookies
     */
    public function setRawCookies($cookies)
    {
    }
}