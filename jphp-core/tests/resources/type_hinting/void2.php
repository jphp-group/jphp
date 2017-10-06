--TEST--
Test void typhinting.
--FILE--
<?php

function test(): void {
}

function test2(): void {
    return;
}

function test3(): void {
    return null;
}

echo "FAIL";

?>
--EXPECTF--

Fatal error: A void function must not return a value in %s on line 11, position %d
