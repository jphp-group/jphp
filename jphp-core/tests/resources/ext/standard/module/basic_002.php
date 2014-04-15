--TEST--
Test php\lang\Module - test only register with classes and functions
--FILE--
<?php

use php\io\Stream;
use php\lang\Module;

$module = new Module(Stream::of('res://resources/ext/standard/module/basic_002.inc.php'));

Foo::bar();
foobar();

?>
--EXPECT--
string(13) "success_class"
string(16) "success_function"
