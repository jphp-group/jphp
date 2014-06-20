<?php
namespace php\net;

/**
 * Class HttpClient
 *
 *  Cloneable
 *
 * @package php\net
 */
class HttpClient {

    /**
     * @return array
     */
    public function getHeaders() { return []; }

    /**
     * @param array $headers
     * @return HttpClient
     */
    public function setHeaders(array $headers) { return $this; }

    /**
     * @param int $value - in milliseconds
     * @return HttpClient
     */
    public function setConnectTimeout($value) { return $this; }

    /**
     * @param int $value - in milliseconds
     * @return HttpClient
     */
    public function setSocketTimeout($value) { return $this; }

    /**
     * @param int $value -1 means that no limits
     * @return HttpClient
     */
    public function setMaxRedirects($value) { return $this; }

    /**
     * @param bool $value
     * @return HttpClient
     */
    public function setRedirectsEnabled($value) { return $this; }

    /**
     * @param string $hostname
     * @param int $port
     * @param string $scheme
     * @return HttpClient
     */
    public function setProxy($hostname, $port = -1, $scheme = 'http') { return $this; }

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

    /**
     * @param $url
     * @return HttpRequest
     */
    public function head($url) { return new HttpRequest(); }

    /**
     * @param $url
     * @return HttpRequest
     */
    public function patch($url) { return new HttpRequest(); }

    /**
     * @param $url
     * @return HttpRequest
     */
    public function options($url) { return new HttpRequest(); }
}

