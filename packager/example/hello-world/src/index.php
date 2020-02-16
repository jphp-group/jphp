<?php
use std;

$hello = new \helloworld\HelloWorld("Hello? World");
$hello->print();


$localPort = \php\net\ServerSocket::findAvailableLocalPort();
$server = new \php\net\ServerSocket($localPort);

while (true) {
    $socket = $server->accept();
}