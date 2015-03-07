<?php
namespace php\framework\web;

/**
 * Class Cookie
 * @package php\framework\web
 */
class HttpCookie
{
    /** @var string */
    public $name;

    /** @var string */
    public $value;

    /** @var string */
    public $domain;

    /** @var string */
    public $path;

    /** @var int */
    public $maxAge;

    /** @var bool */
    public $secure;

    /** @var bool */
    public $httpOnly;
}