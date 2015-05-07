--TEST--
Test basic #2
--FILE--
<?php

use php\io\Stream;
use php\net\ServerSocket;
use php\webserver\WebRequest;
use php\webserver\WebResponse;
use php\webserver\WebServer;

$port = ServerSocket::findAvailableLocalPort();

$server = new WebServer(function(WebRequest $request, WebResponse $response) use ($port) {
    var_dump($request === WebRequest::current());
    var_dump($response === WebResponse::current());

    var_dump($request->method);
    var_dump($request->queryString);
    var_dump($request->url === "http://localhost:$port/foobar/");

    var_dump($request->port == $port);
});

$server->port = $port;
$server->run();

echo (Stream::getContents('http://localhost:' . $server->port . '/foobar/?foobar=123'));

?>
--EXPECTF--
bool(true)
bool(true)
string(3) "GET"
string(10) "foobar=123"
bool(true)
bool(true)