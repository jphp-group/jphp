--TEST--
ZE2 An abstract method may not be called
--FILE--
<?php

class Root {
}

interface MyInterface
{
	function MyInterfaceFunc();
}

abstract class Derived extends Root implements MyInterface {
}

class Leaf extends Derived
{
	function MyInterfaceFunc() {}
}

var_dump(new Leaf);

class Fails extends Root implements MyInterface {
}

?>
===DONE===
--EXPECTF--

Fatal error: Class Fails contains 1 abstract method(s) and must therefore be declared abstract or implement the remaining methods (MyInterface::MyInterfaceFunc) in %s on line %d, position %d
