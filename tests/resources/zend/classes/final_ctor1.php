--TEST--
ZE2 cannot override final __construct
--FILE--
<?php

class Base
{
	public final function __construct()
	{
	}
}

class Works extends Base
{
}

class Extended extends Base
{
	public function Extended()
	{
	}
}

ReflectionClass::export('Extended');

?>
--EXPECTF--

Fatal error: Cannot override final method Base::__construct() with Extended::Extended() in %s on line %d, position %d
