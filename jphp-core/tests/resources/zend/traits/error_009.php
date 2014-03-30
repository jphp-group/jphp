--TEST--
Trying to extend a trait
--FILE--
<?php

trait abc { }

class foo extends abc { }

?>
--EXPECTF--

Fatal error: foo cannot extend from abc - it is not an class in %s on line %d, position %d