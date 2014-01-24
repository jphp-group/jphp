--TEST--
Reflection Class -> getProperty
--FILE--
<?php
class Foo {
    public    $foo  = 1;
    protected $bar  = 2;
    private   $baz  = 3;
    static $foobar = 4;
}

$foo = new Foo();

$r = new ReflectionClass($foo);
var_dump($r->getProperty('foobar'));
var_dump($r->getProperty('foo'));
var_dump($r->getProperty('unknown'));

--EXPECTF--
object(ReflectionProperty)#%d (2) {
  ["name"]=>
  string(6) "foobar"
  ["class"]=>
  string(3) "Foo"
}
object(ReflectionProperty)#%d (2) {
  ["name"]=>
  string(3) "foo"
  ["class"]=>
  string(3) "Foo"
}
NULL
