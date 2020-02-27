--TEST--
Allow abstract function override
--FILE--
<?php

abstract class A           { abstract function bar($x, $y = 0); }
abstract class B extends A { abstract function bar($x); }

echo "DONE";
?>
--EXPECTF--

Fatal error: Declaration of B::bar($x) should be compatible with A::bar($x, $y = 0) in %s on line 4, position %d