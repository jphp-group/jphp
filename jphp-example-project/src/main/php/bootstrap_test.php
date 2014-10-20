<?php

function openLogger() {
    while (true) {
        echo 'Log: ' . yield . "\n";
    }
}

$logger = openLogger();
$logger->send('one');
$logger->send('two');