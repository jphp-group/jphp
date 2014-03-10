--TEST--
Reflection Property -> basic
--FILE--
<?php

class Foo {
    var $var_x = 20;
    private $private_y = 30;
    protected $protected_z = 40;
    public $public_a = 50;
}

$r = new ReflectionProperty('Foo', 'var_x');
var_dump($r->getName());

$r = new ReflectionProperty(new Foo, 'var_x');
var_dump($r->getName());

$r = new ReflectionProperty(new Foo, 'private_y');
var_dump($r->getName());

$r = new ReflectionProperty(new Foo, 'protected_z');
var_dump($r->getName());

$r = new ReflectionProperty(new Foo, 'public_a');
var_dump($r->getName());

try {
    $r = new ReflectionProperty(new Foo, 'unknown');
    var_dump($r->getName());
} catch (ReflectionException $e){
    var_dump($e->getMessage());
}

--EXPECTF--
string(5) "var_x"
string(5) "var_x"
string(9) "private_y"
string(11) "protected_z"
string(8) "public_a"
string(33) "Undefined property: Foo::$unknown"
