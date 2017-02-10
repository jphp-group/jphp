<?php
namespace php\net;

/**
 * Class Proxy
 * @packages std, net
 */
class Proxy
{
    /**
     * @param string $type - DIRECT, HTTP or SOCKS
     * @param string $host
     * @param int $port
     */
    public function __construct($type, $host, $port) { }

    /**
     * @return string host with port
     */
    public function address() { }

    /**
     * @return string DIRECT, HTTP or SOCKS
     */
    public function type() { }
}