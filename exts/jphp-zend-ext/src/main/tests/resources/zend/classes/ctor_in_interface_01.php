--TEST--
ZE2 A class constructor must keep the signature of an interface
--FILE--
<?php
interface constr
{
	function __construct();
}

class implem implements constr
{
	function __construct($a)
	{
	}
}

?>
--EXPECTF--

Fatal error: Declaration of implem::__construct($a) should be compatible with constr::__construct() in %s on line %d, position %d