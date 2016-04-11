<?php
namespace php\net;


/**
 * Class HttpClient
 * @package php\net
 */
class HttpClient
{
    /**
     * @return int in millis
     */
    public function getTimeout()
    {
    }

    /**
     * @param int $value in millis
     */
    public function setTimeout($value)
    {
    }

    /**
     * @return Proxy
     */
    public function getProxy()
    {
    }

    /**
     * @param Proxy|null $proxy
     */
    public function setProxy($proxy)
    {
    }

    /**
     * @param HttpRequest $request
     * @return HttpResponse
     */
    public function send(HttpRequest $request)
    {
    }
}