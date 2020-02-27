--TEST--
Test array type hinting
--FILE--
<?php

function test(array $x, array $y){

}

test(array(), 1);

?>
--EXPECTF--

Fatal error: Uncaught TypeError: Argument 2 passed to test() must be of the type array, int given, called in %s on line 7, position %d and defined in %s on line 3, position %d
Stack Trace:
#0 {main}
  thrown in %s on line 3