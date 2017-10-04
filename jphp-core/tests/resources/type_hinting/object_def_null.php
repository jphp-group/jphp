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

?>
--EXPECTF--
success.

Fatal error: Only variables can be passed by reference in %s on line 10, position %d
