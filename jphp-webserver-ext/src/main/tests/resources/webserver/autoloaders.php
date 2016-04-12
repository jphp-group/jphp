--TEST--
Test response
--FILE--
<?php

use php\io\Stream;
use php\net\ServerSocket;
use php\webserver\WebServer;

class Foobar {

}

spl_autoload_register(function ($class) {
    var_dump($class);
});

$server = new WebServer(function() {
    try {
        new Foobar();
    } catch (Error $e) {
        echo $e->getMessage();
    }
});

$server->importAutoloaders = true;

$server->port = ServerSocket::findAvailableLocalPort();
$server->run();

$conn = null;

echo Stream::getContents("http://localhost:{$server->port}/");


?>
--EXPECT--
string(6) "Foobar"
Class 'Foobar' not found