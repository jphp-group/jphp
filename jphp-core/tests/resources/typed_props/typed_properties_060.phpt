--TEST--
Test typed properties work fine with simple inheritance
--FILE--
<?php

class A {
    public int $a = 1;
}
class B extends A {}

$o = new B;
var_dump($o->a);
$o->a = "a";

?>
--EXPECTF--
int(1)

Fatal error: Uncaught TypeError: Cannot assign string to property A::$a of type int in %s on line 10, position %d
Stack Trace:
#0 {main}
  thrown in %s on line 10