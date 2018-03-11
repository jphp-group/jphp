--TEST--
ZE2 cannot override final old style ctor
--FILE--
<?php

class Base
{
	public final function Base()
	{
	}
}

class Works extends Base
{
}

class Extended extends Base
{
	public function __construct()
	{
	}
}

?>
--EXPECTF--

Fatal error: Cannot override final method Base::Base() with Extended::__construct() in %s on line %d, position %d