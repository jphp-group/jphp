--TEST--
Test callable type hinting
--FILE--
<?php

function test(callable $x, callable $y){

}

test('sin', 1);
?>
--EXPECTF--

Fatal error: Uncaught TypeError: Argument 2 passed to test() must be of the type callable, int given, called in %s on line 7, position %d and defined in %s on line 3, position %d
Stack Trace:
#0 {main}
  thrown in %s on line 3