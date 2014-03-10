--TEST--
Reflection Class -> getExtension
--FILE--
<?php

class Foo {
}

$reflectionClass = new ReflectionClass('stdClass');
var_dump($reflectionClass->getExtension());
var_dump($reflectionClass->getExtensionName());
$reflectionClass = new ReflectionClass('foo');
var_dump($reflectionClass->getExtension());
var_dump($reflectionClass->getExtensionName());

--EXPECTF--
object(ReflectionExtension)#%d (1) {
  ["name"]=>
  string(4) "Core"
}
string(4) "Core"
NULL
bool(false)
