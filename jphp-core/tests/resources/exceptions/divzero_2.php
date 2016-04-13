--TEST--
Mod by zero 2
--FILE--
<?php

$z = 20 % 0;
echo "Fail";
?>
--EXPECTF--

Fatal error: Uncaught DivisionByZeroError: Modulo by zero in %s on line 3, position 8
Stack Trace:
#0 {main}
  thrown in %s on line 3