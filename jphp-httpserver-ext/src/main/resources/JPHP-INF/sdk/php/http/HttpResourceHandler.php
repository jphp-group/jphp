<?php
namespace php\http;

use php\io\File;

/**
 * @package php\http
 * @packages http
 */
class HttpResourceHandler extends HttpAbstractHandler
{
    /**
     * HttpFileDirectoryHandler constructor.
     * @param string|File $file
     */
    public function __construct(string $file)
    {
    }

    /**
     * @param string|File $file [optional]
     * @return string
     */
    public function file(string $file): string
    {
    }

    /**
     * @param bool $value [optional]
     * @return bool
     */
    public function etags(bool $value): bool
    {
    }

    /**
     * @param bool $value [optional]
     * @return bool
     */
    public function acceptRanges(bool $value): bool
    {
    }

    /**
     * @param bool $value [optional]
     * @return bool
     */
    public function dirAllowed(bool $value): bool
    {
    }

    /**
     * @param bool $value [optional]
     * @return bool
     */
    public function directoriesListed(bool $value): bool
    {
    }

    /**
     * @param bool $value [optional]
     * @return bool
     */
    public function pathInfoOnly(bool $value): bool
    {
    }

    /**
     * @param bool $value [optional]
     * @return bool
     */
    public function redirectWelcome(bool $value): bool
    {
    }

    /**
     * @param string $value [optional]
     * @return string
     */
    public function cacheControl(string $value): string
    {
    }

    /**
     * @param HttpServerRequest $request
     * @param HttpServerResponse $response
     */
    public function __invoke(HttpServerRequest $request, HttpServerResponse $response): void
    {
    }
}