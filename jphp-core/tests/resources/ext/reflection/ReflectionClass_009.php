--TEST--
Reflection Class -> getModifiers
--FILE--
<?php

class A { }
final class B { }
abstract class C { }

$r = new ReflectionClass('A');
var_dump($r->getModifiers());

$r = new ReflectionClass('B');
var_dump($r->getModifiers());

$r = new ReflectionClass('C');
var_dump($r->getModifiers());

var_dump(ReflectionClass::IS_IMPLICIT_ABSTRACT);
var_dump(ReflectionClass::IS_EXPLICIT_ABSTRACT);
var_dump(ReflectionClass::IS_FINAL);

--EXPECTF--
int(0)
int(64)
int(32)
int(16)
int(32)
int(64)
