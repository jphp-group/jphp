<?php
namespace php\net;

/**
 * Class HttpClient
 * @package php\net
 */
class HttpClient {

    /**
     * @return array
     */
    public function getHeaders() { return []; }

    /**
     * @param array $headers
     */
    public function setHeaders(array $headers) { }

    /**
     * @param $url
     * @return HttpRequest
     */
    public function get($url) { return new HttpRequest(); }

    /**
     * @param $url
     * @return HttpRequest
     */
    public function post($url) { return new HttpRequest(); }

    /**
     * @param $url
     * @return HttpRequest
     */
    public function put($url) { return new HttpRequest(); }

    /**
     * @param $url
     * @return HttpRequest
     */
    public function delete($url) { return new HttpRequest(); }
}

