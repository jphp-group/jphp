--TEST--
Test basic #1
--FILE--
<?php

use php\io\Stream;
use php\net\ServerSocket;
use php\webserver\WebServer;

$server = new WebServer(function() {
    echo "Hello World";
});

$server->port = ServerSocket::findAvailableLocalPort();
$server->run();

var_dump(Stream::getContents('http://localhost:' . $server->port));

?>
--EXPECTF--
string(11) "Hello World"