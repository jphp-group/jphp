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
Warning: Declaration of B::f($x) should be compatible with A::f() in %s on line %d at pos %s
===DONE===