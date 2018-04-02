<?php
namespace ssh;

use php\io\Stream;

/**
 * Class SSHChannel
 * @package ssh
 * @packages ssh
 */
class SSHChannel
{
    /**
     * @readonly
     * @var int
     */
    public $id;

    /**
     * @readonly
     * @var int
     */
    public $exitStatus;

    private function __construct()
    {
    }

    /**
     * @param int $timeout [optional]
     */
    public function connect(int $timeout): void
    {
    }

    /**
     *
     */
    public function disconnect(): void
    {
    }

    /**
     * @return bool
     */
    public function isClosed(): bool
    {
    }

    /**
     * @return bool
     */
    public function isConnected(): bool
    {
    }

    /**
     * @return bool
     */
    public function isEOF(): bool
    {
    }

    /**
     * @param string $signal
     */
    public function sendSignal(string $signal): void
    {
    }

    public function start()
    {
    }


    /**
     * @return null|Stream
     */
    public function getInputStream(): ?Stream
    {
    }

    /**
     * @param Stream $stream
     * @param bool $dontClose
     */
    public function setInputStream(?Stream $stream, bool $dontClose = false)
    {
    }

    /**
     * @return null|Stream
     */
    public function getOutputStream(): ?Stream
    {
    }

    /**
     * @param Stream $stream
     * @param bool $dontClose
     */
    public function setOutputStream(?Stream $stream, bool $dontClose = false)
    {
    }


}