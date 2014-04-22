--TEST--
Test php\lang\Module - test include with variables
--FILE--
<?php

use php\io\Stream;
use php\lang\Module;

$module = new Module(Stream::of('res://resources/ext/standard/module/basic_003.inc.php'));

$foo = 'fail';
$bar = 'fail';
$undefined = 'fail';

$module->call(['foo' => 'success_foo', 'bar' => 'success_bar']);

?>
--EXPECT--
string(11) "success_foo"
string(11) "success_bar"
NULL
