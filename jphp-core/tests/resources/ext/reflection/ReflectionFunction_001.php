--TEST--
Reflection Function -> getName
--FILE--
<?php

function foobar(){}

$reflectionClass = new ReflectionFunction('coS');
var_dump($reflectionClass->getName());

$reflectionClass = new ReflectionFunction('fooBar');
var_dump($reflectionClass->getName());

$reflectionClass = new ReflectionFunction(function(){});
var_dump($reflectionClass->getName());

--EXPECTF--
string(3) "cos"
string(6) "foobar"
bool(false)
