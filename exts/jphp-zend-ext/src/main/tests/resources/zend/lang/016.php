--TEST--
Testing user-defined function in included file
--FILE--
<?php
include __DIR__ . "/016.inc";
MyFunc("Hello");
?>
--EXPECT--
Hello