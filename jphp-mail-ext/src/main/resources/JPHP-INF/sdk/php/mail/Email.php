<?php
namespace php\mail;
use php\io\Stream;
use php\io\File;

/**
 * Class Email
 * @package php\mail
 */
class Email
{
    const __PACKAGE__ = 'mail';

    /**
     * @param string $email
     * @param string $name (optional)
     * @param string $charset (optional)
     * @return $this
     */
    public function setFrom($email, $name, $charset)
    {
        // native.
        return $this;
    }

    /**
     * @param string $charset
     * @return $this
     */
    public function setCharset($charset)
    {
        // native.
        return $this;
    }

    /**
     * @param string $subject
     * @return $this
     */
    public function setSubject($subject)
    {
        // native.
        return $this;
    }

    /**
     * @param array $addresses
     * @return $this
     */
    public function setTo(array $addresses)
    {
        // native.
        return $this;
    }

    /**
     * @param array $addresses
     * @return $this
     */
    public function setCc(array $addresses)
    {
        // native.
        return $this;
    }

    /**
     * @param array $addresses
     * @return $this
     */
    public function setBcc(array $addresses)
    {
        // native.
        return $this;
    }

    /**
     * @param array $email
     * @return $this
     */
    public function setBounceAddress($email)
    {
        // native.
        return $this;
    }

    /**
     * @param array $headers
     * @return $this
     */
    public function setHeaders($headers)
    {
        return $this;
    }

    /**
     * @param string $message
     * @return $this
     */
    public function setMessage($message)
    {
        // native.
        return $this;
    }

    /**
     * @param string $message
     * @return $this
     */
    public function setHtmlMessage($message)
    {
        // native.
        return $this;
    }

    /**
     * @param string $message
     * @return $this
     */
    public function setTextMessage($message)
    {
        // native.
        return $this;
    }

    /**
     * @param string|File|Stream $content
     * @param string $contentType
     * @param string $name
     * @param string $description
     * @return $this
     */
    public function attach($content, $contentType, $name, $description = '')
    {
        // native.
        return $this;
    }

    /**
     * Sends the email. Internally we build a MimeMessage
     * which is afterwards sent to the SMTP server.
     *
     * @param EmailBackend $backend
     * @return string the message id of the underlying MimeMessage
     */
    public function send(EmailBackend $backend) {}
}