--TEST--
Reflection Class -> getInterfaces
--FILE--
<?php

interface C { }
interface A extends C { }
interface B { }

class Bar implements C {

}

class Foo extends Bar implements A, B {

}

$r = new ReflectionClass('foo');
$arr = $r->getInterfaces();
var_dump(count($arr));
var_dump($arr[0]->getName());
var_dump($arr[1]->getName());
var_dump($arr[2]->getName());

$r = new ReflectionClass('bar');
$arr = $r->getInterfaces();
$arr = $r->getInterfaces();
var_dump(count($arr));
var_dump($arr[0]->getName());

--EXPECTF--
int(3)
string(1) "C"
string(1) "A"
string(1) "B"
int(1)
string(1) "C"
