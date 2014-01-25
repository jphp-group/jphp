--TEST--
Reflection Property -> getDeclaringClass ?
--FILE--
<?php

class Foo {
    function test($x){}
}

$r = new ReflectionMethod('foo', 'test');
var_dump($r->getParameters()[0]);
var_dump($r->getParameters()[0]->getDeclaringClass());


--EXPECTF--
object(ReflectionParameter)#%d (1) {
  ["name"]=>
  string(1) "x"
}
object(ReflectionClass)#%d (1) {
  ["name"]=>
  string(3) "Foo"
}