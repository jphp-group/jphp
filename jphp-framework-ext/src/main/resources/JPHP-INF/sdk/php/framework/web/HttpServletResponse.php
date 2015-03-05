<?php
namespace php\framework\web;

/**
 * Class HttpServletRequest
 * @package php\framework\web
 */
abstract class HttpServletResponse
{
    /**
     * @var int
     */
    public $status;

    /**
     * @param Cookie $cookie
     */
    public function addCookie(Cookie $cookie) {}
}