--TEST--
Test object type hinting with null def
--FILE--
<?php

class A { }

function test(A $x = null, A &$y = null){
}

test();
echo "success.\n";
test(null, null);

--EXPECTF--
success.

Recoverable error: Argument 1 passed to test() must be an instance of A, NULL given in %s on line %d, position %d
