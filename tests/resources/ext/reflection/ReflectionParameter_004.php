--TEST--
Reflection Property -> getClass ?
--FILE--
<?php

class Foo { }
class Bar { }

function foobar(Foo $x, Bar $y, Unknown $z, $a, array $b, callable $c){}


$r = new ReflectionParameter('foobar', 0);
var_dump($r->getClass());

$r = new ReflectionParameter('foobar', 1);
var_dump($r->getClass());

$r = new ReflectionParameter('foobar', 2);
var_dump($r->getClass());


$r = new ReflectionParameter('foobar', 3);
var_dump($r->getClass());


$r = new ReflectionParameter('foobar', 4);
var_dump($r->getClass());


$r = new ReflectionParameter('foobar', 5);
var_dump($r->getClass());


--EXPECTF--
object(ReflectionClass)#%d (1) {
  ["name"]=>
  string(3) "Foo"
}
object(ReflectionClass)#%d (1) {
  ["name"]=>
  string(3) "Bar"
}
NULL
NULL
NULL
NULL