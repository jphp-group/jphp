--TEST--
Mod by zero
--FILE--
<?php

$x = 20;
$y = 0;

$z = $x % $y;
echo "Fail";
?>
--EXPECTF--

Fatal error: Uncaught DivisionByZeroError: Modulo by zero in %s on line 6, position 9
Stack Trace:
#0 {main}
  thrown in %s on line 6