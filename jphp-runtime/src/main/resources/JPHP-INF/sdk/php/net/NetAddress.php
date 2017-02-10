<?php
namespace php\net;
use php\io\IOException;

/**
 * Class NetAddress
 * @packages std, net
 */
class NetAddress
{
    const __PACKAGE__ = 'std, net';

    /**
     * @param string $host
     * @return NetAddress[]
     * @throws IOException
     */
    public static function getAllByName($host)
    {
    }

    /**
     * @param int[] $address
     * @return NetAddress
     * @throws IOException
     */
    public static function getByAddress(array $address)
    {
    }

    /**
     * Returns the loopback address.
     *
     * The NetAddress returned will represent the IPv4
     * loopback address, 127.0.0.1, or the IPv6 loopback
     * address, ::1. The IPv4 loopback address returned
     * is only one of many in the form 127.*.*.*
     *
     * @return NetAddress
     * @throws IOException
     */
    public static function getLoopbackAddress()
    {
    }

    /**
     * Returns the address of the local host. This is achieved by retrieving
     * the name of the host from the system, then resolving that name into
     * an NetAddress.
     *
     * @return NetAddress
     * @throws IOException
     */
    public static function getLocalHost()
    {
    }

    /**
     * Determines the IP address of a host, given the host's name.
     *
     * @param string $host
     * @throws IOException
     */
    public function __construct($host)
    {
    }

    /**
     * Gets the host name for this IP address.
     * @return string
     */
    public function hostName()
    {
    }

    /**
     * Returns the IP address string in textual presentation.
     * @return string
     */
    public function hostAddress()
    {
    }

    /**
     * Returns the raw IP address of this NetAddress
     * object. The result is in network byte order: the highest order
     * byte of the address is in address()[0].
     *
     * @return int[]
     */
    public function address()
    {
    }

    /**
     * Gets the fully qualified domain name for this IP address.
     * Best effort method, meaning we may not be able to return
     * the FQDN depending on the underlying system configuration.
     *
     * @return string
     */
    public function canonicalHostName()
    {
    }

    /**
     * @return string
     */
    public function __toString()
    {
    }

    /**
     * Test whether that address is reachable. Best effort is made by the
     * implementation to try to reach the host, but firewalls and server
     * configuration may block requests resulting in a unreachable status
     * while some specific ports may be accessible.
     * A typical implementation will use ICMP ECHO REQUESTs if the
     * privilege can be obtained, otherwise it will try to establish
     * a TCP connection on port 7 (Echo) of the destination host.
     *
     * @param int $timeout in millis.
     * @return bool
     * @throws IOException if a network error occurs
     */
    public function isReachable($timeout)
    {
    }

    /**
     * @return bool
     */
    public function isSiteLocalAddress()
    {
    }

    /**
     * @return bool
     */
    public function isMulticastAddress()
    {
    }

    /**
     * @return bool
     */
    public function isAnyLocalAddress()
    {
    }

    /**
     * @return bool
     */
    public function isLoopbackAddress()
    {
    }

    /**
     * @return bool
     */
    public function isLinkLocalAddress()
    {
    }

    /**
     * @return bool
     */
    public function isMCGlobal()
    {
    }

    /**
     * @return bool
     */
    public function isMCNodeLocal()
    {
    }

    /**
     * @return bool
     */
    public function isMCLinkLocal()
    {
    }

    /**
     * @return bool
     */
    public function isMCSiteLocal()
    {
    }

    /**
     * @return bool
     */
    public function isMCOrgLocal()
    {
    }
}