--TEST--
ZE2 __toString() in __destruct/exception
JPHP - Supports exceptions in __toString
--FILE--
<?php

class Test
{
	function __toString()
	{
		throw new Exception("It's normal for JPHP");
		return "Hello\n";
	}

	function __destruct()
	{
		echo $this;
	}
}

try
{
	$o = new Test;
	unset($o);
}
catch(Exception $e)
{
	var_dump($e->getMessage());
}

?>
====DONE====
--EXPECTF--
string(20) "It's normal for JPHP"
====DONE====
