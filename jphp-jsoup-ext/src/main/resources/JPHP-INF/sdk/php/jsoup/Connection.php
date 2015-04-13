<?php
namespace php\jsoup;

/**
 * Class Connection
 * @package php\jsoup
 */
class Connection {
    const METHOD_POST = 'POST';
    const METHOD_GET  = 'GET';

    /**
     * @param array $data
     * @return Connection
     */
    public function data(array $data) { }

    /**
     * @param array $data
     * @return Connection
     */
    public function cookies(array $data) { }

    /**
     * @param array $data
     * @return Connection
     */
    public function headers(array $data) { }

    /**
     * @param string $url
     * @return Connection
     */
    public function url($url) { }

    /**
     * @param string $method POST or GET
     * @return Connection
     */
    public function method($method) { }

    /**
     * @return ConnectionResponse
     */
    public function execute() { }

    /**
     *
     * @return Document
     */
    public function get() { }

    /**
     *
     * @return Document
     */
    public function post() { }
} 