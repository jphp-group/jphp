--TEST--
Reflection Function -> getClosure
--FILE--
<?php

function foobar($x, $y){ echo "$x:$y\n"; }

$r = new ReflectionFunction('abs');
$func = $r->getClosure();
var_dump($func(-10));

$r = new ReflectionFunction('fooBar');
$func = $r->getClosure();
$func(100, 500);

$r = new ReflectionFunction(function($x){return "|$x|";});
$func = $r->getClosure();
var_dump($func("foobar"));

--EXPECTF--
int(10)
100:500
string(8) "|foobar|"
