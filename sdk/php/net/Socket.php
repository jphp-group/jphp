<?php
namespace php\net;
use php\io\IOException;
use php\io\MiscStream;
use php\io\Stream;

/**
 * Class Socket
 * @package php\net
 */
class Socket {
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
}
