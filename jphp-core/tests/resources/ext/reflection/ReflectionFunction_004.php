--TEST--
Reflection Function -> invoke
--FILE--
<?php

function foobar($x, $y){ echo "$x:$y\n"; }

$r = new ReflectionFunction('abs');
var_dump($r->invoke(-10));
var_dump($r->invokeArgs([-5]));

$r = new ReflectionFunction('fooBar');
$r->invoke(100, 500);
$r->invokeArgs([200, 600]);

$r = new ReflectionFunction(function($x){return "|$x|";});
var_dump($r->invoke("foo"));
var_dump($r->invokeArgs(["bar"]));

--EXPECTF--
int(10)
int(5)
100:500
200:600
string(5) "|foo|"
string(5) "|bar|"
