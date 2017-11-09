<?php
namespace php\http;

use php\io\File;

/**
 * Class HttpDownloadFileHandler
 * @package php\http
 * @packages http
 */
class HttpRedirectHandler extends HttpAbstractHandler
{
    /**
     * HttpDownloadFileHandler constructor.
     * @param string $url
     * @param bool $permanently
     */
    public function __construct(string $url, bool $permanently = false)
    {
    }

    /**
     * @return string
     */
    public function url(): string
    {
    }

    /**
     * @return bool
     */
    public function permanently(): bool
    {
    }

    /**
     * @param string $url
     * @param bool $permanently
     */
    public function reset(string $url, bool $permanently = false)
    {
    }

    /**
     * @param HttpServerRequest $request
     * @param HttpServerResponse $response
     * @return bool
     */
    public function __invoke(HttpServerRequest $request, HttpServerResponse $response): bool
    {
    }
}