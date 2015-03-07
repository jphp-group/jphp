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

    /**
     * @var HttpPart[]
     */
    public $parts;

    /**
     * @param string $name
     * @return HttpPart
     */
    public function getPart($name) {}

    /**
     * @param bool $create (optional)
     */
    public function getSession($create) {}

    /**
     * @return bool
     */
    public function isRequestedSessionIdValid() {}

    /**
     * @return bool
     */
    public function isRequestedSessionIdFromCookie() {}

    /**
     * @return bool
     */
    public function isRequestedSessionIdFromURL() {}

    /**
     * @param HttpServletResponse $response
     */
    public function authenticate($response) {}

    /**
     * @param string $login
     * @param string $password
     */
    public function login($login, $password) {}

    /**
     * ...
     */
    public function logout() {}
}