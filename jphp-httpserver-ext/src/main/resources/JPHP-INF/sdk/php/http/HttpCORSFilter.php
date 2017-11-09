<?php
namespace php\http;

/**
 * @package php\http
 * @packages http
 */
class HttpCORSFilter extends HttpAbstractHandler
{
    /**
     * HttpCORSFilter constructor.
     * @param array $allowedDomains
     * @param array $allowedMethods
     * @param array $allowedHeaders
     * @param bool $allowedCredentials
     * @param int $maxAge
     */
    public function __construct($allowedDomains = ['*'],
                                $allowedMethods = ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS'],
                                $allowedHeaders = [], $allowedCredentials = true, $maxAge = 0)
    {
    }

    /**
     * @return array
     */
    public function allowDomains(): array
    {
    }

    /**
     * @return array
     */
    public function allowMethods(): array
    {
    }

    /**
     * @return array
     */
    public function allowHeaders(): array
    {
    }

    /**
     * @return bool
     */
    public function allowCredentials(): bool
    {
    }

    /**
     * @return int
     */
    public function maxAge(): int
    {
    }

    /**
     * Handle OPTIONS query.
     * @param HttpServerRequest $request
     * @param HttpServerResponse $response
     */
    public function handleOptions(HttpServerRequest $request, HttpServerResponse $response): void
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