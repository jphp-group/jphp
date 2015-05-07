--TEST--
Test response
--FILE--
<?php

use php\net\ServerSocket;
use php\net\URL;
use php\webserver\WebRequest;
use php\webserver\WebResponse;
use php\webserver\WebServer;

$server = new WebServer(function(WebRequest $request, WebResponse $response) {
    $response->writeToBody('foobar');
    $response->status = 404;
});

$server->port = ServerSocket::findAvailableLocalPort();
$server->run();

$conn = null;

$conn = (new URL("http://localhost:{$server->port}/"))->openConnection();

$conn->requestMethod = 'GET';

$conn->setRequestProperty('Content-Type', 'text/html');
$conn->setRequestProperty('Accept', 'text/plain');

$conn->connect();

var_dump($conn->responseCode);
var_dump($conn->getErrorStream()->readFully());

?>
--EXPECT--
int(404)
string(6) "foobar"
