<?php
namespace php\webserver;

use php\io\Stream;

/**
 * Class WebRequest
 * @package php\webserver
 */
class WebRequest
{
    const __PACKAGE__ = 'net, webserver';

    /**
     * @readonly
     * @var string
     */
    public $method;

    /**
     * @readonly
     * @var string
     */
    public $scheme;

    /**
     * @readonly
     * @var string
     */
    public $pathInfo;

    /**
     * @readonly
     * @var string
     */
    public $servletPath;

    /**
     * @readonly
     * @var string
     */
    public $queryString;

    /**
     * @readonly
     * @var string
     */
    public $authType;

    /**
     * @readonly
     * @var string
     */
    public $url;

    /**
     * @readonly
     * @var int
     */
    public $port;

    /**
     * @readonly
     * @var string
     */
    public $ip;

    /**
     * Array of arrays [name, value, path, domain, httpOnly, secure, maxAge, comment]
     * @readonly
     * @var array
     */
    public $cookies;

    /**
     * @param WebRequest $parent
     */
    protected function __construct(WebRequest $parent) {
    }

    /**
     * @return string
     */
    public function getBody() {}

    /**
     * @return Stream
     */
    public function getBodyStream() {}

    /**
     * @return string
     */
    protected function getMethod()
    {
        return $this->method;
    }

    /**
     * @return string
     */
    protected function getPathInfo()
    {
        return $this->pathInfo;
    }

    /**
     * @return string
     */
    protected function getAuthType()
    {
        return $this->authType;
    }

    /**
     * @return string
     */
    protected function getQueryString()
    {
        return $this->queryString;
    }

    /**
     * @return string
     */
    protected function getUrl()
    {
        return $this->url;
    }

    /**
     * @return string
     */
    protected function getScheme()
    {
        return $this->scheme;
    }

    /**
     * @return int
     */
    protected function getPort()
    {
        return $this->port;
    }

    /**
     * @return string
     */
    protected function getIp()
    {
        return $this->ip;
    }

    /**
     * @return WebRequest
     */
    public static function current()
    {
    }
}