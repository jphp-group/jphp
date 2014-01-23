--TEST--
Reflection Parameter -> new
--FILE--
<?php

class My {
    function test($i){ }
}

function foobar($x, $y){

}

$r = new ReflectionParameter('foobar', 1);
var_dump($r->getName());

$r = new ReflectionParameter('foobar', 'x');
var_dump($r->getName());

$r = new ReflectionParameter(function($anon_a, $anon_b){ }, 0);
var_dump($r->getName());

$r = new ReflectionParameter(function($anon_a, $anon_b){ }, 'anon_b');
var_dump($r->getName());

try {
    $r = new ReflectionParameter('foobar', 'z');
} catch (ReflectionException $e){
    echo $e->getMessage(), "\n";
}

$r = new ReflectionParameter([new My, 'test'], 0);
var_dump($r->getName());

$r = new ReflectionParameter(['My', 'test'], 0);
var_dump($r->getName());

$r = new ReflectionParameter('My::test', 0);
var_dump($r->getName());


--EXPECTF--
string(1) "y"
string(1) "x"
string(6) "anon_a"
string(6) "anon_b"
Parameter z does not exist
string(1) "i"
string(1) "i"
string(1) "i"
