--TEST--
Reflection Function -> number of params
--FILE--
<?php

function foobar($x, $y, $z = 1, $a = 3){}

$reflectionClass = new ReflectionFunction('srand');
var_dump($reflectionClass->getNumberOfParameters());
var_dump($reflectionClass->getNumberOfRequiredParameters());

$reflectionClass = new ReflectionFunction('fooBar');
var_dump($reflectionClass->getNumberOfParameters());
var_dump($reflectionClass->getNumberOfRequiredParameters());

$reflectionClass = new ReflectionFunction(function($a, $b, $c, $d = 3) use ($x, $y) {});
var_dump($reflectionClass->getNumberOfParameters());
var_dump($reflectionClass->getNumberOfRequiredParameters());

--EXPECTF--
int(1)
int(0)
int(4)
int(2)
int(4)
int(3)
