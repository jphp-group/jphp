<?php
namespace ssh;
use php\io\File;
use php\io\Stream;

/**
 * Class SSH
 * @package ssh
 *
 * @packages ssh
 */
class SSH
{
    public function __construct()
    {
    }

    /**
     * @param string $username
     * @param string $host
     * @param int $port
     * @return SSHSession
     */
    public function getSession(string $username, string $host, int $port = 22): SSHSession
    {
    }

    /**
     * @param string $host
     * @param int $port
     * @return SSHSession
     */
    public function openSession(string $host, int $port = 22): SSHSession
    {
    }

    /**
     * @param Stream|File|string $source
     */
    public function setKnownHosts($source)
    {
    }

    /**
     * @param string $prvkey
     * @param string $passphrase
     */
    public function addIdentity(string $prvkey, string $passphrase)
    {
    }

    /**
     *
     */
    public function removeAllIdentity()
    {
    }
}