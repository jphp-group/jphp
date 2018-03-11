--TEST--
default argument value in and in implementing class with interface in included file
--FILE--
<?php
include __DIR__ . '/interface_optional_arg_003.inc';
include __DIR__ . '/interface_optional_arg_003_2.inc';

?>
--EXPECTF--
int(2)