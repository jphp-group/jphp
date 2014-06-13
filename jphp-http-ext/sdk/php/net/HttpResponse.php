<?php
namespace php\net;

/**
 * Class HttpResponse
 * @package php\net
 */
class HttpResponse {

    public function __construct() { }

    /**
     * @return int
     */
    public function getStatusCode() { return 0; }

    /**
     * @return string
     */
    public function getContent() { return ""; }
}
