--TEST--
Reflection Method -> getDeclaringClass
--FILE--
<?php

class Foo {
    function test(){}
    function test2(){}
}

class Bar extends Foo {
    function test2(){}
}

$r = new ReflectionMethod('Bar', 'test');
var_dump($r->getDeclaringClass());

$r = new ReflectionMethod('Bar', 'test2');
var_dump($r->getDeclaringClass());

$r = new ReflectionMethod('Foo', 'test2');
var_dump($r->getDeclaringClass());

--EXPECTF--
object(ReflectionClass)#%d (1) {
  ["name"]=>
  string(3) "Foo"
}
object(ReflectionClass)#%d (1) {
  ["name"]=>
  string(3) "Bar"
}
object(ReflectionClass)#%d (1) {
  ["name"]=>
  string(3) "Foo"
}