<?php
namespace php\http;

/**
 * Class HttpServer
 * @package php\http
 */
class HttpServer
{
    /**
     * Server constructor.
     * @param int $port (optional)
     * @param string $host (optional)
     */
    public function __construct($port = null, $host = null)
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
     * @param array $settings [base=>string, cacheControl=>string, dirAllowed=>bool, dirsListed=>bool, welcomeFile=>string, etags=>bool, acceptRanges=>bool]
     */
    public function addResourceHandler(array $settings)
    {
    }
}