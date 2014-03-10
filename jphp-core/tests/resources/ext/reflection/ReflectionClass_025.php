--TEST--
Reflection Class -> getStaticProperty
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
var_dump($r->getStaticProperties());

--EXPECTF--
array(1) {
  [0]=>
  object(ReflectionProperty)#%d (2) {
    ["name"]=>
    string(6) "foobar"
    ["class"]=>
    string(3) "Foo"
  }
}
