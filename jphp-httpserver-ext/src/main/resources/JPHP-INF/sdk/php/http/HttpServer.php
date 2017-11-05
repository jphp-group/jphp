<?php
namespace php\http;

/**
 * Class HttpServer
 * @package php\http
 * @packages http
 */
class HttpServer
{
    /**
     * Server constructor.
     * @param int $port [optional]
     * @param string $host [optional]
     */
    public function __construct($port = null, $host = null)
    {
    }

    /**
     * If true, this server instance will be explicitly stopped when the
     * JVM is shutdown. Otherwise the JVM is stopped with the server running.
     *
     * @param bool $value [optional]
     * @return bool
     */
    public function stopAtShutdown(bool $value): bool
    {
    }

    /**
     * @param int $min [optional]
     * @return int
     */
    public function minThreads(int $min): int
    {
    }

    /**
     * @param int $max [optional]
     * @return int
     */
    public function maxThreads(int $max): int
    {
    }

    /**
     * @param int $timeout [optional]
     * @return int
     */
    public function threadIdleTimeout(int $timeout): int
    {
    }

    /**
     * Add connector to server:
     *  127.0.0.1:8080
     *  8080, 127.0.0.1
     *
     * @param string $portOrHostPort
     */
    public function listen($portOrHostPort)
    {
    }

    /**
     * Run server in current thread.
     */
    public function run()
    {
    }

    /**
     * Run server in background thread.
     */
    public function runInBackground()
    {
    }

    /**
     * Stop server.
     */
    public function shutdown()
    {
    }

    /**
     * @param callable $callback
     */
    public function addHandler(callable $callback)
    {
    }

    /**
     * Remove all handlers.
     */
    public function clearHandlers()
    {
    }

    /**
     * @param array $settings [base=>string, cacheControl=>string, dirAllowed=>bool, dirsListed=>bool, welcomeFile=>string, etags=>bool, acceptRanges=>bool]
     */
    public function addResourceHandler(array $settings)
    {
    }

    /**
     * @param callable|null $handler ($request, $response)
     */
    public function setRequestLogHandler(?callable $handler)
    {
    }

    /**
     * Route a handler by method and path.
     *
     * @param string|array $methods
     * @param string $path
     * @param callable $handler
     * @return HttpRouteHandler
     */
    public function route($methods, string $path, callable $handler): HttpRouteHandler
    {
    }

    /**
     * Route any methods.
     *
     * @param string $path
     * @param callable $handler
     * @return HttpRouteHandler
     */
    public function any(string $path, callable $handler): HttpRouteHandler
    {
    }

    /**
     * Route a handler by GET method + path.
     * @param string $path
     * @param callable $handler
     * @return HttpRouteHandler
     */
    public function get(string $path, callable $handler): HttpRouteHandler
    {
    }

    /**
     * Route a handler by POST method + path.
     * @param string $path
     * @param callable $handler
     * @return HttpRouteHandler
     */
    public function post(string $path, callable $handler): HttpRouteHandler
    {
    }

    /**
     * Route a handler by PUT method + path.
     * @param string $path
     * @param callable $handler
     * @return HttpRouteHandler
     */
    public function put(string $path, callable $handler): HttpRouteHandler
    {
    }

    /**
     * Route a handler by DELETE method + path.
     * @param string $path
     * @param callable $handler
     * @return HttpRouteHandler
     */
    public function delete(string $path, callable $handler): HttpRouteHandler
    {
    }

    /**
     * Route a handler by OPTIONS method + path.
     * @param string $path
     * @param callable $handler
     * @return HttpRouteHandler
     */
    public function options(string $path, callable $handler): HttpRouteHandler
    {
    }
}