--TEST--
Test basic request #2
--FILE--
<?php

use php\io\Stream;
use php\net\NetStream;
use php\net\ServerSocket;
use php\net\URL;
use php\webserver\WebRequest;
use php\webserver\WebServer;

$server = new WebServer(function(WebRequest $request) {
    echo "|{$request->getBodyStream()->readFully()}|";
});

$server->port = ServerSocket::findAvailableLocalPort();
$server->run();

$conn = (new URL("http://localhost:{$server->port}/"))->openConnection();

$conn->doOutput = true;
$conn->requestMethod = 'POST';

$conn->setRequestProperty('Content-Type', 'application/json');
$conn->setRequestProperty('Accept', 'application/json');

$stream = $conn->getOutputStream();
$stream->write('foo');
$stream->write('bar');
$stream->close();

var_dump($conn->getInputStream()->readFully());

?>
--EXPECT--
string(8) "|foobar|"
