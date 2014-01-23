--TEST--
Reflection Class -> getStartLine
--FILE--
<?php

  class Foo {

}

 class Bar {

}

$r = new ReflectionClass('Foo');
var_dump($r->getStartLine());
var_dump($r->getPosition());

$r = new ReflectionClass('Bar');
var_dump($r->getStartLine());
var_dump($r->getPosition());

$r = new ReflectionClass('ReflectionClass');
var_dump($r->getStartLine());
var_dump($r->getPosition());

--EXPECTF--
int(3)
int(3)
int(7)
int(2)
bool(false)
bool(false)
