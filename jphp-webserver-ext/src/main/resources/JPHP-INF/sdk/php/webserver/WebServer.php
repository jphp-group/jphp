<?php
namespace php\webserver;

/**
 * Embedded http web server.
 *
 * Class WebServer
 * @package php\webserver
 */
class WebServer
{
    /**
     * @param callable $onRequest
     */
    public function __construct(callable $onRequest)
    {
    }

    /**
     * Run server.
     */
    public function run()
    {
    }

    /**
     * @param array $handler [path, location, cache, cachePeriod, gzip]
     */
    public function addStaticHandler(array $handler)
    {
    }

    /**
     * @param int $port
     * @return WebServer
     */
    public function setPort($port)
    {
        return $this;
    }
}