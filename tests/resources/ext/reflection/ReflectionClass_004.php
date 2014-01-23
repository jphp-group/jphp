--TEST--
Reflection Class -> getName
--FILE--
<?php

class Foo {
}

$reflectionClass = new ReflectionClass('Foo');
var_dump($reflectionClass->getName());
var_dump(new ReflectionClass(new Foo)->getName());
var_dump(new ReflectionClass(new stdClass)->getName());
var_dump(new ReflectionClass('StdClass')->getName());

--EXPECT--
string(3) "Foo"
string(3) "Foo"
string(8) "stdClass"
string(8) "stdClass"
