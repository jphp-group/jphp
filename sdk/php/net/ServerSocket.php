<?php
namespace php\net;

use php\io\IOException;

/**
 * Class SocketServer
 * @package php\net
 */
class ServerSocket {

    /**
     * @param int $port
     * @param int $backLog
     */
    public function __construct($port, $backLog = 50) { }

    /**
     * @return Socket
     * @throws IOException
     */
    public function accept() { }
}
