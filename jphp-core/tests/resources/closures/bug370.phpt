--TEST--
Bug #370
--FILE--
<?php
$a = function() use (&$a): bool { return true; };

var_dump($a());
?>
--EXPECT--
bool(true)