<?php
namespace php\mail;

/**
 * Class EmailBackend
 * @package php\mail
 */
class EmailBackend
{
    const __PACKAGE__ = 'mail';

    /**
     * The host name of the SMTP server.
     * @var string
     */
    public $hostName;

    /**
     * The listening port of the SMTP server.
     * @var string
     */
    public $smtpPort;

    /**
     * The current SSL port used by the SMTP transport.
     * @var string
     */
    public $sslSmtpPort;

    /**
     * Sending partial email.
     * @var bool
     */
    public $sendPartial;

    /**
     * The socket I/O timeout value in milliseconds.
     * @var int
     */
    public $socketTimeout;

    /**
     * The socket connection timeout value in milliseconds.
     * @var int
     */
    public $socketConnectionTimeout;

    /**
     * Whether SSL/TLS encryption for the transport is currently enabled (SMTPS/POPS).
     * @var bool
     */
    public $sslOnConnect;

    /**
     * Whether the server identity is checked as specified by RFC 2595.
     * @var bool
     */
    public $sslCheckServerIdentity;

    /**
     * @return string
     */
    protected function getHostName()
    {
        return $this->hostName;
    }

    /**
     * @param string $hostName
     */
    protected function setHostName($hostName)
    {
        $this->hostName = $hostName;
    }

    /**
     * @return string
     */
    protected function getSmtpPort()
    {
        return $this->smtpPort;
    }

    /**
     * @param string $smtpPort
     */
    protected function setSmtpPort($smtpPort)
    {
        $this->smtpPort = $smtpPort;
    }

    /**
     * @return string
     */
    protected function getSslSmtpPort()
    {
        return $this->sslSmtpPort;
    }

    /**
     * @param string $sslSmtpPort
     */
    protected function setSslSmtpPort($sslSmtpPort)
    {
        $this->sslSmtpPort = $sslSmtpPort;
    }

    /**
     * @return boolean
     */
    protected function isSendPartial()
    {
        return $this->sendPartial;
    }

    /**
     * @param boolean $sendPartial
     */
    protected function setSendPartial($sendPartial)
    {
        $this->sendPartial = $sendPartial;
    }

    /**
     * @return int
     */
    protected function getSocketTimeout()
    {
        return $this->socketTimeout;
    }

    /**
     * @param int $socketTimeout
     */
    protected function setSocketTimeout($socketTimeout)
    {
        $this->socketTimeout = $socketTimeout;
    }

    /**
     * @return int
     */
    protected function getSocketConnectionTimeout()
    {
        return $this->socketConnectionTimeout;
    }

    /**
     * @param int $socketConnectionTimeout
     */
    protected function setSocketConnectionTimeout($socketConnectionTimeout)
    {
        $this->socketConnectionTimeout = $socketConnectionTimeout;
    }

    /**
     * @return boolean
     */
    protected function isSslOnConnect()
    {
        return $this->sslOnConnect;
    }

    /**
     * @param boolean $sslOnConnect
     */
    protected function setSslOnConnect($sslOnConnect)
    {
        $this->sslOnConnect = $sslOnConnect;
    }

    /**
     * @return boolean
     */
    protected function isSslCheckServerIdentity()
    {
        return $this->sslCheckServerIdentity;
    }

    /**
     * @param boolean $sslCheckServerIdentity
     */
    protected function setSslCheckServerIdentity($sslCheckServerIdentity)
    {
        $this->sslCheckServerIdentity = $sslCheckServerIdentity;
    }

    /**
     * Sets the userName and password if authentication is needed. If this
     * method is not used, no authentication will be performed.
     *
     * @param string $login
     * @param string $password
     */
    public function setAuthentication($login, $password)
    {
        // native.
    }

    /**
     * Disable authorization.
     */
    public function clearAuthentication()
    {
        // native.
    }
}