<?php
namespace php\net;

/**
 * Class Proxy
 * @package php\net
 */
class Proxy
{
    const __PACKAGE__ = 'std, net';

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