--TEST--
Reflection Class -> implementsInterface
--FILE--
<?php

interface A {}
interface B extends A {}
interface C { }

class Bar implements C {}
class Foo implements B {}

echo "\nCheck B:\n";
$r = new ReflectionClass('B');
var_dump($r->implementsInterface('A'));
var_dump($r->implementsInterface('B'));

echo "\nCheck C:\n";
$r = new ReflectionClass('C');
var_dump($r->implementsInterface('C'));

echo "\nCheck Bar:\n";
$r = new ReflectionClass('Bar');
var_dump($r->implementsInterface('C'));

echo "\nCheck Foo:\n";
$r = new ReflectionClass('Foo');
var_dump($r->implementsInterface('B'));
var_dump($r->implementsInterface('A'));

try {
    var_dump($r->implementsInterface('Foo'));
} catch (ReflectionException $e){
    var_dump($e->getMessage());
}

--EXPECT--

Check B:
bool(true)
bool(true)

Check C:
bool(true)

Check Bar:
bool(true)

Check Foo:
bool(true)
bool(true)
string(24) "Interface Foo is a Class"
