<?php
namespace php\http;

/**
 * @package php\http
 * @packages http
 */
class HttpGzipFilter extends HttpAbstractHandler
{
    /**
     * HttpGzipFilter constructor.
     */
    public function __construct()
    {
    }

    /**
     * @param array $methods
     */
    public function includeMethods(array $methods)
    {
    }

    /**
     * @param array $methods
     */
    public function excludeMethods(array $methods)
    {
    }

    /**
     * @param array $mimeTypes
     */
    public function includeMimeTypes(array $mimeTypes)
    {
    }

    /**
     * @param array $mimeTypes
     */
    public function excludeMimeTypes(array $mimeTypes)
    {
    }

    /**
     * @param int $size
     */
    public function minGzipSize(int $size)
    {
    }

    /**
     * @param int $level
     */
    public function compressLevel(int $level)
    {
    }

    /**
     * @param HttpServerRequest $request
     * @param HttpServerResponse $response
     * @return bool true if router is matches request.
     */
    public function __invoke(HttpServerRequest $request, HttpServerResponse $response): bool
    {
    }
}