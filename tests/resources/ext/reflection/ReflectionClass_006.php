--TEST--
Reflection Class -> getFileName
--FILE--
<?php

class Foo {
}

$reflectionClass = new ReflectionClass('stdClass');
var_dump($reflectionClass->getFileName());
$reflectionClass = new ReflectionClass('foo');
var_dump($reflectionClass->getFileName());

--EXPECTF--
bool(false)
string(%d) %s
