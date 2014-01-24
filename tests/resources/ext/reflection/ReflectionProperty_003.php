--TEST--
Reflection Property -> getDeclaringClass ?
--FILE--
<?php

class Bar {
    public $dynamic;
}

class Foo extends Bar {
    protected static $static;
}

$r = new ReflectionProperty('Foo', 'dynamic');
var_dump($r->getDeclaringClass());

$r = new ReflectionProperty('Bar', 'dynamic');
var_dump($r->getDeclaringClass());

$r = new ReflectionProperty('Foo', 'static');
var_dump($r->getDeclaringClass());


--EXPECTF--
object(ReflectionClass)#%d (1) {
  ["name"]=>
  string(3) "Bar"
}
object(ReflectionClass)#%d (1) {
  ["name"]=>
  string(3) "Bar"
}
object(ReflectionClass)#%d (1) {
  ["name"]=>
  string(3) "Foo"
}
