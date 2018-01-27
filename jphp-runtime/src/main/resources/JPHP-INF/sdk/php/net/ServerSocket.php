<?php
namespace php\net;

use php\io\IOException;

/**
 * Class SocketServer
 * @packages std, net
 */
class ServerSocket
{

    /**
     * @param int $port
     * @param int $backLog
     */
    public function __construct($port = null, $backLog = 50)
    {
    }

    /**
     * @return Socket
     * @throws IOException
     */
    public function accept()
    {
    }

    /**
     * @param string $hostname
     * @param int $port
     * @param int $backLog
     * @throws SocketException
     */
    public function bind($hostname, $port, $backLog = 50)
    {
    }

    /**
     * @throws IOException
     */
    public function close()
    {
    }

    /**
     * @return bool
     */
    public function isClosed()
    {
    }

    /**
     * Returns the binding state of the ServerSocket.
     * @return bool
     */
    public function isBound()
    {
    }

    /**
     * Enable/disable SO_TIMEOUT with the specified timeout, in
     * milliseconds.
     * @param int $timeout
     * @throws SocketException
     */
    public function setSoTimeout($timeout)
    {
    }

    /**
     * Enable/disable the SO_REUSEADDR socket option.
     * @param bool $on
     * @throws SocketException
     */
    public function setReuseAddress($on)
    {
    }

    /**
     * @param int $size
     * @throws SocketException
     */
    public function setReceiveBufferSize($size)
    {
    }

    /**
     * Sets performance preferences for this ServerSocket.
     *
     *  ! Not implemented yet for TCP/IP
     *
     * @param int $connectTime
     * @param int $latency
     * @param int $bandWidth
     */
    public function setPerformancePreferences($connectTime, $latency, $bandWidth)
    {
    }

    /**
     * @return int
     */
    public static function findAvailableLocalPort()
    {
    }

    /**
     * @param int $port
     * @return bool
     */
    public static function isAvailableLocalPort(int $port): bool
    {
    }
}
