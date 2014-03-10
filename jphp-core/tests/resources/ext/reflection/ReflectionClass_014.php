--TEST--
Reflection Class -> has
--FILE--
<?php

interface A {
    const C = 40;

    function test1();
}

abstract class Foo implements A {
    const A = 20;
    var $x;
    private $z;
    private static function test2(){}
}

class Bar extends Foo {
    const B = 30;
    protected $y;

    public function test1(){}
}

echo "Constants exists:\n";
$r = new ReflectionClass('Bar');
var_dump($r->hasConstant('B'));
var_dump($r->hasConstant('A'));
var_dump($r->hasConstant('C'));
var_dump($r->hasConstant('D'));

echo "\nMethod exists:\n";
var_dump($r->hasMethod('test1'));
var_dump($r->hasMethod('test2'));
var_dump($r->hasMethod('test3'));

$r = new ReflectionClass('A');
var_dump($r->hasMethod('test1'));

$r = new ReflectionClass('Foo');
var_dump($r->hasMethod('test1'));
var_dump($r->hasMethod('test2'));

echo "\nProperty exists:\n";
$r = new ReflectionClass('Bar');
var_dump($r->hasProperty('x'));
var_dump($r->hasProperty('z'));
var_dump($r->hasProperty('y'));
var_dump($r->hasProperty('D'));

--EXPECTF--
Constants exists:
bool(true)
bool(true)
bool(true)
bool(false)

Method exists:
bool(true)
bool(false)
bool(false)
bool(true)
bool(true)
bool(true)

Property exists:
bool(true)
bool(false)
bool(true)
bool(false)
