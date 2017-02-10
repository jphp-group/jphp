<?php
namespace php\net;
use php\io\IOException;
use php\io\MiscStream;
use php\io\Stream;

/**
 * Class Socket
 * @packages std, net
 */
class Socket
{
    /**
     * @param null|string $host
     * @param null|int $port
     */
    public function __construct($host = null, $port = null) { }

    /**
     * @return MiscStream
     * @throws IOException
     */
    public function getOutput() { }

    /**
     * @return MiscStream
     * @throws IOException
     */
    public function getInput() { }

    /**
     * @return string
     */
    public function getLocalAddress() { }

    /**
     * @return string
     */
    public function getAddress() { }

    /**
     * @return int
     */
    public function getLocalPort() { }

    /**
     * @return int
     */
    public function getPort() { }

    /**
     * @throws IOException
     */
    public function close() { }

    /**
     * @throws IOException
     */
    public function shutdownInput() { }

    /**
     * @throws IOException
     */
    public function shutdownOutput() { }

    /**
     * @return bool
     */
    public function isConnected() { }

    /**
     * @return bool
     */
    public function isClosed() { }

    /**
     * @return bool
     */
    public function isBound() { }

    /**
     * @return bool
     */
    public function isInputShutdown() { }

    /**
     * @return bool
     */
    public function isOutputShutdown() { }

    /**
     * Connects this socket to the server
     * @param string $hostname
     * @param int $port
     * @param null|int $timeout
     */
    public function connect($hostname, $port, $timeout = null) { }

    /**
     * Binds the socket to a local address.
     *
     * @param string $hostname
     * @param int $port
     * @throws SocketException
     */
    public function bind($hostname, $port) { }

    /**
     * the system will pick up
     * an ephemeral port and a valid local address to bind the socket.
     */
    public function bindDefault() { }

    /**
     * Enable/disable SO_TIMEOUT with the specified timeout, in
     * milliseconds.
     * @param int $timeout
     * @throws SocketException
     */
    public function setSoTimeout($timeout) { }

    /**
     * @param bool $on
     * @param int $linger
     * @throws SocketException
     */
    public function setSoLinger($on, $linger) { }

    /**
     * Enable/disable the SO_REUSEADDR socket option.
     * @param bool $on
     * @throws SocketException
     */
    public function setReuseAddress($on) { }

    /**
     * @param int $size
     * @throws SocketException
     */
    public function setReceiveBufferSize($size) { }

    /**
     * @param bool $on
     * @throws SocketException
     */
    public function setTcpNoDelay($on) { }

    /**
     * @param bool $on
     * @throws SocketException
     */
    public function setKeepAlive($on) { }

    /**
     * @param bool $on
     * @throws SocketException
     */
    public function setOOBInline($on) { }

    /**
     * @param int $size
     * @throws SocketException
     */
    public function setSendBufferSize($size) { }

    /**
     * Sets traffic class or type-of-service octet in the IP
     * header for packets sent from this Socket.
     * @param int $tc
     */
    public function setTrafficClass($tc) { }

    /**
     * Sets performance preferences for this ServerSocket.
     *
     *  ! Not implemented yet for TCP/IP
     *
     * @param int $connectTime
     * @param int $latency
     * @param int $bandWidth
     */
    public function setPerformancePreferences($connectTime, $latency, $bandWidth) { }

    /**
     * Send one byte of urgent data on the socket. The byte to be sent is the lowest eight
     * bits of the data parameter.
     *
     * @param int $data
     * @throws SocketException
     */
    public function sendUrgentData($data) { }
}
