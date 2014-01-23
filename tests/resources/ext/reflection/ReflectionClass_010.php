--TEST--
Reflection Class -> getName
--FILE--
<?php

namespace foobar {

    class A { }

}

class B {

}

$r = new ReflectionClass('foobar\A');
var_dump($r->getName());
var_dump($r->getShortName());
var_dump($r->getNamespaceName());
var_dump($r->isNamespace());

$r = new ReflectionClass('B');
var_dump($r->getName());
var_dump($r->getShortName());
var_dump($r->getNamespaceName());
var_dump($r->isNamespace());

--EXPECTF--
string(8) "foobar\A"
string(1) "A"
string(6) "foobar"
bool(true)
string(1) "B"
string(1) "B"
bool(false)
bool(false)
