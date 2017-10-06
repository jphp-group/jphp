--TEST--
Test void typhinting.
--FILE--
<?php

function test(): ?void {
}

echo "FAIL";

?>
--EXPECTF--

Fatal error: Void type cannot be nullable in %s on line 1, position %d
