--TEST--
ZE2 $this can be an argument to a static function

JPHP: Changed, another behavior
--FILE--
<?php

class TestClass
{
	function __construct()
	{
		self::Test1();
		$this->Test1();
	}

	static function Test1()
	{
		var_dump($this);
	}

	static function Test2($this)
	{
		var_dump($this);
	}
}

$obj = new TestClass;
TestClass::Test2(new stdClass);

?>
===DONE===
--EXPECTF--

Fatal error: Cannot re-assign $this in %s on line 16, position %d
