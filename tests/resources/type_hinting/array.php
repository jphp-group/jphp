--TEST--
Test array type hinting
--FILE--
<?php

function test(array $x, array $y){

}

test(array(), 1);

--EXPECTF--

Recoverable error: Argument 2 passed to test() must be of the type array, integer given in %s on line %d, position %d
