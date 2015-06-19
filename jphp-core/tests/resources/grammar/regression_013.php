--TEST--
Testing for regression with encapsed variables in class declaration context
--FILE--
<?php

class A { function foo() { return "{${$a}}"; } function list() {} }

echo "Done";

?>
--EXPECTF--
Done