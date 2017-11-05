<?php
namespace php\http;

use php\io\Stream;
use php\util\Locale;

/**
 * Class HttpServerResponse
 * @package php\http
 * @packages http
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
    public function status(int $status, string $message = null): HttpServerResponse
    {
    }

    /**
     * @param string $value
     * @return HttpServerResponse
     */
    public function contentType(string $value): HttpServerResponse
    {
    }

    /**
     * @param int $value
     * @return HttpServerResponse
     */
    public function contentLength(int $value): HttpServerResponse
    {
    }

    /**
     * @param string $encoding
     * @return HttpServerResponse
     */
    public function charsetEncoding(string $encoding): HttpServerResponse
    {
    }

    /**
     * @param mixed $content
     * @param string $charset
     * @return HttpServerResponse
     */
    public function write(string $content, string $charset = 'UTF-8'): HttpServerResponse
    {
    }

    /**
     * @param string $content
     * @param string $charset
     * @return HttpServerResponse
     */
    public function body(string $content, string $charset = 'UTF-8'): HttpServerResponse
    {
    }

    /**
     * @param string $name
     * @param string $value
     * @return HttpServerResponse
     */
    public function header(string $name, string $value): HttpServerResponse
    {
    }

    /**
     * @param string $location
     * @return HttpServerResponse
     */
    public function redirect(string $location): HttpServerResponse
    {
    }

    /**
     * Cookie as array:
     *      [
     *       name => string (required), value => string,
     *       path => string, domain => string,
     *       comment => string, secure => bool, httpOnly => bool, version => int
     *      ]
     *
     * @param array $cookie
     * @return HttpServerResponse
     */
    public function addCookie(array $cookie): HttpServerResponse
    {
    }

    /**
     * @param string $url
     * @return string
     */
    public function encodeRedirectUrl(string $url): string
    {
    }

    /**
     * @param string $url
     * @return string
     */
    public function encodeUrl(string $url): string
    {
    }

    /**
     * Output Stream for body.
     * @return Stream
     */
    public function bodyStream(): Stream
    {
    }

    /**
     * @param Locale $locale
     * @return HttpServerResponse
     */
    public function locale(Locale $locale): HttpServerResponse
    {
    }

    /**
     * Forces any content in the buffer to be written to the client.  A call
     * to this method automatically commits the response, meaning the status
     * code and headers will be written.
     * @return HttpServerResponse
     */
    public function flush(): HttpServerResponse
    {
    }
}