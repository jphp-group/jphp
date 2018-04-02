<?php
namespace ssh;

/**
 * Class SSHSession
 * @package ssh
 * @packages ssh
 */
class SSHSession
{
    /**
     * @var string
     */
    public $clientVersion;

    /**
     * @var string
     */
    public $host;

    /**
     * @var int
     */
    public $port;

    /**
     * @var int
     */
    public $timeout;

    private function __construct()
    {
    }

    /**
     * @param string $key
     * @param string $value
     */
    public function setConfig(string $key, string $value): void
    {
    }

    /**
     * @param string $key
     * @return string
     */
    public function getConfig(string $key): string
    {
    }

    /**
     * @param int $timeout [optional]
     */
    public function connect(int $timeout)
    {
    }

    /**
     *
     */
    public function disconnect()
    {
    }

    /**
     * param is:
     *  [
     *      getPassphrase => callable(): string,
     *      getPassword => callable(): string,
     *      promptPassword => callable(string $msg): bool
     *      promptPassphrase => callable(string $msg): bool
     *      promptYesNo => callable(string $msg): bool
     *      showMessage => callable(string $msg): void
     *  ]
     * @param array $userInfoHandlers
     */
    public function setUserInfo(?array $userInfoHandlers)
    {
    }

    /**
     * @return array|null
     */
    public function getUserInfo(): ?array
    {
    }

    /**
     * @param string $password
     */
    public function setPassword(string $password): void
    {
    }


    /**
     * @param bool $enable
     */
    public function setDaemonThread(bool $enable): void
    {
    }


    /**
     * @throws SSHException
     */
    public function sendIgnore(): void
    {
    }

    /**
     * @throws SSHException
     */
    public function sendKeepAliveMsg(): void
    {
    }

    /**
     * @throws SSHException
     */
    public function rekey(): void
    {
    }

    /**
     * Open exec channel.
     *
     * @return SSHExecChannel
     */
    public function exec(): SSHExecChannel
    {
    }

    /**
     * Open SFTP channel.
     *
     * @return SSHSftpChannel
     */
    public function sftp(): SSHSftpChannel
    {
    }
}