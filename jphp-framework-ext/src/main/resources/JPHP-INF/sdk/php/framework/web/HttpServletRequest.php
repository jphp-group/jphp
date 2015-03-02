<?php
namespace php\framework\web;

/**
 * Class HttpServletRequest
 * @package php\framework\web
 */
abstract class HttpServletRequest
{
    /**
     * @var string
     * @readonly
     */
    public $authType;

    /**
     * @var string
     * @readonly
     */
    public $method;

    /**
     * @var string
     * @readonly
     */
    public $pathInfo;

    /**
     * @var string
     * @readonly
     */
    public $pathTranslated;

    /**
     * @var string
     * @readonly
     */
    public $contextPath;

    /**
     * @var string
     */
    public $queryString;

    /**
     * @var string
     */
    public $remoteUser;

    /**
     * @var string
     */
    public $requestedSessionId;

    /**
     * @var string
     */
    public $requestURI;

    /**
     * @var string
     */
    public $servletPath;
}