--TEST--
It's not possible to directly instantiate the Generator class
--FILE--
<?php
new Generator;
?>
--EXPECTF--

Fatal error: Cannot instantiate abstract class Generator in %s on line %d, position %d