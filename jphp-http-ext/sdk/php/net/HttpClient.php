<?php
namespace php\net;

/**
 * Class HttpClient
 * @package php\net
 */
class HttpClient {

    /**
     * @param $url
     * @return HttpRequest
     */
    public function get($url) { return new HttpRequest(); }
}

$client = new HttpClient();
$response = $client->get('http://foobar.ru')->execute();
