<?php
namespace ssh;
use php\io\Stream;

/**
 * Class SSHExecChannel
 * @package ssh
 *
 * @packages ssh
 */
class SSHExecChannel extends SSHChannel
{
    /**
     * @param string $command
     */
    public function setCommand(string $command): void
    {
    }

    /**
     * @return null|Stream
     */
    public function getErrorStream(): ?Stream
    {
    }

    /**
     * @param Stream $stream
     * @param bool $dontClose
     */
    public function setErrorStream(Stream $stream, bool $dontClose = false)
    {
    }
}