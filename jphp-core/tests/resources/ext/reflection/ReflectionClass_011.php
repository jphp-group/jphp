--TEST--
Reflection Class -> getParentClass
--FILE--
<?php

class Foo {

}

class Bar extends Foo {

}

$r = new ReflectionClass('Foo');
var_dump($r->getParentClass());

$r = new ReflectionClass('Bar');
var_dump($r->getParentClass()->getName());

$r = new ReflectionClass('ReflectionClass');
var_dump($r->getParentClass()->getName());

--EXPECTF--
NULL
string(3) "Foo"
string(10) "Reflection"
