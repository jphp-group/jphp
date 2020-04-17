<?php
namespace php\http;
use php\io\IOException;
use php\io\Stream;
use php\lang\IllegalStateException;
use php\lang\JavaException;
use php\util\Locale;

/**
 * Class HttpServerRequest
 * @package php\http
 * @packages http
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
     * @return string
     */
    public function httpVersion(): string
    {
    }

    /**
     * http, https
     * @return string
     */
    public function protocol(): string
    {
    }

    /**
     * @param string $name
     * @return string
     */
    function header(string $name): string
    {
    }

    /**
     * @param bool $lowerKeys
     * @return array
     */
    function headers($lowerKeys = false): array
    {
    }

    /**
     * @param string $name
     * @return array
     */
    function cookie(string $name): array
    {
    }

    /**
     * @return array
     */
    function cookies(): array
    {
    }

    /**
     * @param string $name
     * @return string
     */
    function param(string $name): string
    {
    }

    /**
     * @param string $name
     * @param $value [optional]
     * @return mixed
     */
    function attribute(string $name, $value)
    {
    }

    /**
     * @param string $name [optional]
     * @return string
     */
    function query(string $name): string
    {
    }

    /**
     * @return string
     */
    function queryEncoding(): string
    {
    }

    /**
     * @return array
     */
    function queryParameters(): array
    {
    }

    /**
     * @return string
     */
    function path(): string
    {
    }

    /**
     * @return string
     */
    function method(): string
    {
    }

    /**
     * @return string
     */
    function sessionId(): string
    {
    }

    /**
     * @return string
     */
    function localAddress(): string
    {
    }

    /**
     * @return int
     */
    function localPort(): int
    {
    }

    /**
     * @return string
     */
    function localName(): string
    {
    }

    /**
     * @return string
     */
    function remoteAddress(): string
    {
    }

    /**
     * @return string
     */
    function remoteUser(): string
    {
    }

    /**
     * @return string
     */
    function remoteHost(): string
    {
    }

    /**
     * @return string
     */
    function remotePort(): string
    {
    }

    /**
     * @return Locale
     */
    function locale(): Locale
    {
    }

    /**
     * @return Locale[]
     */
    function locales(): array
    {
    }

    /**
     * @return Stream
     * @throws IllegalStateException
     */
    function bodyStream(): Stream
    {
    }

    /**
     * @return HttpPart[]
     * @throws JavaException
     * @throws IOException
     */
    function getParts(): array
    {
    }
}