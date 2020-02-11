<?php
use std;

$hello = new \helloworld\HelloWorld("Hello? World");
//$hello->print();

function test($a, $b, $c) {
    return $a + $b + $c;
}

function foo() {
    for ($i = 0; $i < 10000000; $i++) {
        $r = test($i, $i, $i);
    }
}

$t = Time::millis();
foo();
echo (Time::millis() - $t) . "ms\n";