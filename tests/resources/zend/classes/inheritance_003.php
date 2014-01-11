--TEST--
ZE2 method inheritance without interfaces
--FILE--
<?php

class A
{
	function f($x) {}
}

class B extends A
{
	function f() {}
}

?>
===DONE===
--EXPECTF--
Strict Standards: Declaration of B::f() must be compatible with A::f($x) in %s on line %d at pos %d
===DONE===