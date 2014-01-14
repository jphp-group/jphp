--TEST--
ZE2 interface and __construct
--FILE--
<?php

class MyObject {}

interface MyInterface
{
	public function __construct(MyObject $o);
}

class MyTestClass implements MyInterface
{
	public function __construct(MyObject $o)
	{
	}
}

$obj = new MyTestClass;

?>
===DONE===
--EXPECTF--

Recoverable error: Argument 1 passed to MyTestClass::__construct() must be an instance of MyObject, none given, called in %s on line 17, position %d and defined in %s on line 12, position %d
