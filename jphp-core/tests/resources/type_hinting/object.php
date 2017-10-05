--TEST--
Test object type hinting
--FILE--
<?php

class A { }
class B extends A { }

function test(A $x, B $y, C $z){
}

test(new B, new B, new A);

?>
--EXPECTF--

Fatal error: Uncaught TypeError: Argument 3 passed to test() must be an instance of C, instance of A given, called in %s on line 9, position %d and defined in %s on line 6, position %d
Stack Trace:
#0 {main}
  thrown in %s on line 6