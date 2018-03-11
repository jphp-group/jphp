<?php
namespace php\jsoup;
use php\net\Proxy;

/**
 * Class Connection
 * @package php\jsoup
 */
abstract class Connection
{
    const __PACKAGE__ = 'jsoup';

    const METHOD_POST = 'POST';
    const METHOD_GET = 'GET';

    /**
     * @param array $data
     * @return Connection
     */
    public function data(array $data)
    {
    }

    /**
     * @param array $data
     * @return Connection
     */
    public function cookies(array $data)
    {
    }

    /**
     * @param array $data
     * @return Connection
     */
    public function headers(array $data)
    {
    }

    /**
     * @param string $name
     * @param string $value
     * @return Connection
     */
    public function header($name, $value)
    {
    }

    /**
     * @param string $url
     * @return Connection
     */
    public function url($url)
    {
    }

    /**
     * @param string $method POST or GET
     * @return Connection
     */
    public function method($method)
    {
    }

    /**
     * @param Proxy $proxy
     * @return Connection
     */
    public function proxy(Proxy $proxy)
    {
    }

    /**
     * @param string $userAgent
     * @return Connection
     */
    public function userAgent($userAgent)
    {
    }

    /**
     * @param int $bytes
     * @return Connection
     */
    public function maxBodySize($bytes)
    {
    }

    /**
     * @param int $millis
     * @return Connection
     */
    public function timeout($millis)
    {
    }

    /**
     * @param string $referrer
     * @return Connection
     */
    public function referrer($referrer)
    {
    }

    /**
     * @param bool $enable
     * @return Connection
     */
    public function followRedirects($enable)
    {
    }

    /**
     * @param bool $enable
     * @return Connection
     */
    public function ignoreHttpErrors($enable)
    {
    }

    /**
     * @param bool $enable
     * @return Connection
     */
    public function ignoreContentType($enable)
    {
    }

    /**
     * @return ConnectionResponse
     */
    public function execute()
    {
    }

    /**
     *
     * @return Document
     */
    public function get()
    {
    }

    /**
     *
     * @return Document
     */
    public function post()
    {
    }

    /**
     * @return ConnectionRequest
     */
    public function request()
    {
    }

    /**
     * @return ConnectionResponse
     */
    public function response()
    {
    }
} 