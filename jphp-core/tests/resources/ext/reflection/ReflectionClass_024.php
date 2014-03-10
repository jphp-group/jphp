--TEST--
Reflection Class -> getStaticPropertyValue
--FILE--
<?php
define('MY', 333);

class Foo {
    static $foobar = MY;
    static $x;
}

$foo = new Foo();

$r = new ReflectionClass($foo);
var_dump($r->getStaticPropertyValue('foobar'));
var_dump($r->getStaticPropertyValue('x'));

try {
    var_dump($r->getStaticPropertyValue('undefined'));
} catch (ReflectionException $e){
    var_dump($e->getMessage());
}

--EXPECT--
int(333)
bool(false)
string(35) "Undefined property: Foo::$undefined"
