--TEST--
ZE2 method inheritance without interfaces
--FILE--
<?php

class A
{
	function f() {}
}

class B extends A
{
	function f($x) {}
}

?>
===DONE===
--EXPECTF--
Strict Standards: Declaration of B::f() must be compatible with A::f() in %s on line %d at pos %s
===DONE===