<?php
namespace php\http;

/**
 * @package php\http
 * @packages http
 */
class HttpRouteFilter extends HttpAbstractHandler
{
    /**
     * HttpRouteHandler constructor.
     * @param string|array $methods
     * @param string $path
     * @param callable $handler
     */
    public function __construct($methods, string $path, callable $handler)
    {
    }

    /**
     * @return callable
     */
    public function handler(): callable
    {
    }

    /**
     * @return array
     */
    public function methods(): array
    {
    }

    /**
     * @return string
     */
    public function path(): string
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