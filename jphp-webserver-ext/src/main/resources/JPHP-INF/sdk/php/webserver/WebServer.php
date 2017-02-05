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
    const __PACKAGE__ = 'net, webserver';

    /**
     * @readonly
     * @var string
     */
    public $id;

    /** @var int */
    public $port = 8080;

    /** @var bool */
    public $isolated = true;

    /** @var bool */
    public $importAutoloaders = true;

    /** @var bool */
    public $hotReload = true;

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
     * @return WebServer
     */
    public function addStaticHandler(array $handler)
    {
    }

    /**
     * @return string
     */
    protected function getId()
    {
        return $this->id;
    }

    /**
     * @return int
     */
    protected function getPort()
    {
        return $this->port;
    }

    /**
     * @param int $port
     */
    protected function setPort($port)
    {
        $this->port = $port;
    }

    /**
     * @return boolean
     */
    protected function isIsolated()
    {
        return $this->isolated;
    }

    /**
     * @param boolean $isolated
     */
    protected function setIsolated($isolated)
    {
        $this->isolated = $isolated;
    }

    /**
     * @return boolean
     */
    protected function isImportAutoloaders()
    {
        return $this->importAutoloaders;
    }

    /**
     * @param boolean $importAutoloaders
     */
    protected function setImportAutoloaders($importAutoloaders)
    {
        $this->importAutoloaders = $importAutoloaders;
    }

    /**
     * @return boolean
     */
    protected function isHotReload()
    {
        return $this->hotReload;
    }

    /**
     * @param boolean $hotReload
     */
    protected function setHotReload($hotReload)
    {
        $this->hotReload = $hotReload;
    }

    /**
     * Unable to clone.
     */
    private function __clone()
    {
    }

    /**
     * @return WebServer
     */
    public static function current()
    {
    }
}