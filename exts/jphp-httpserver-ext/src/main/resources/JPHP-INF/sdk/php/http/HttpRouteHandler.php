<?php
namespace php\http;

/**
 * Class HttpRouteHandler
 * @package php\http
 * @packages http
 */
class HttpRouteHandler extends HttpRouteFilter
{
    /**
     * @param HttpServerRequest $request
     * @param HttpServerResponse $response
     * @return bool true if router is matches request.
     */
    public function __invoke(HttpServerRequest $request, HttpServerResponse $response): bool
    {
    }
}