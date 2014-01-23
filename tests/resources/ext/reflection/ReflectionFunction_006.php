--TEST--
Reflection Function -> getParameters
--FILE--
<?php

define('MY_CONST', 100500);
class Foo { const BAR = 1; }

function foobar($x, array $y, callable $z, $a = "foobar", $b = MY_CONST, $c = Foo::BAR){ }

$r = new ReflectionFunction('fooBar');
var_dump(sizeof($args = $r->getParameters()));

var_dump($args[0]->getName());
var_dump($args[1]->getName());
var_dump($args[1]->isArray());
var_dump($args[2]->isCallable());
var_dump($args[3]->getDefaultValue());
var_dump($args[4]->getDefaultValue());
var_dump($args[4]->getDefaultValueConstantName());
var_dump($args[5]->getDefaultValue());
var_dump($args[5]->getDefaultValueConstantName());

--EXPECTF--
int(6)
string(1) "x"
string(1) "y"
bool(true)
bool(true)
string(6) "foobar"
int(100500)
string(8) "MY_CONST"
int(1)
string(8) "Foo::BAR"
