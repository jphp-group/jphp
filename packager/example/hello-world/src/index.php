<?php

$t = \php\time\Time::millis();

function test($a, $b, $c = 33, $d = 22) {
    $x = $a * 2;
    $y = $b ^ 4;
    return $c + $d + 1 + $x - $y;
}

echo \php\time\Time::millis() - $t, "ms";
/*

$localPort = \php\net\ServerSocket::findAvailableLocalPort();
$server = new \php\net\ServerSocket($localPort);

while (true) {
    $socket = $server->accept();
}*/