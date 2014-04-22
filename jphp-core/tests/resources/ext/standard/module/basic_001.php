--TEST--
Test php\lang\Module - simple
--FILE--
<?php

use php\io\Stream;
use php\lang\Module;

$module = new Module(Stream::of('res://resources/ext/standard/module/basic_001.inc.php'));

echo "--check-include\n";
var_dump($module->call());

echo "--check-include-retry\n";
var_dump($module->call());
?>
--EXPECT--
--check-include
string(7) "success"
--check-include-retry
string(7) "success"
