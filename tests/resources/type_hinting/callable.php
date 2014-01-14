--TEST--
Test callable type hinting
--FILE--
<?php

function test(callable $x, callable $y){

}

test('sin', 1);

--EXPECTF--

Recoverable error: Argument 2 passed to test() must be of the type callable, integer given, called in %s on line 7, position %d and defined in %s on line 3, position %d
