<?php
namespace php\framework\web;

/**
 * Class HttpServletRequest
 * @package php\framework\web
 */
abstract class HttpServletResponse
{
    /**
     * @param Cookie $cookie
     */
    public function addCookie(Cookie $cookie) {}

    /**
     * @param string $name
     * @return bool
     */
    public function containsHeader($name) {}

    /**
     * @param string $url
     * @return bool
     */
    public function encodeURL($url) {}

    /**
     * @param string $url
     * @return bool
     */
    public function encodeRedirectURL($url) {}

    /**
     * @param int $sc
     * @param string $msg (optional)
     */
    public function sendError($sc, $msg) {}

    /**
     * @param string $location
     */
    public function sendRedirect($location) {}

    /**
     * @param string $name
     * @param string $value
     */
    public function setHeader($name, $value) {}

    /**
     * @param string $name
     * @param string $value
     */
    public function addHeader($name, $value) {}

    /**
     * @param $name
     * @return string
     */
    public function getHeader($name) {}

    /**
     * @param string $name
     * @return string[]
     */
    public function getHeaders($name) {}

    /**
     * @return string
     */
    public function getHeaderNames() {}

    /**
     * @param int $sc
     * @param string $msg (optional)
     */
    public function setStatus($sc, $msg) {}

    /**
     * @return int
     */
    public function getStatus() {}
}