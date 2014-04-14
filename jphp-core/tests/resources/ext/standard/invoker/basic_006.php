--TEST--
Invoker basic test of description
--FILE--
<?php

use php\lang\Invoker;

$invoker = Invoker::of('var_dump');
var_dump($invoker->getDescription());

$invoker = Invoker::of(function($a, array $b, callable $c, Invoker $d, $f = 'foobar'){});
var_dump($invoker->getDescription());

function test($x, $y) {}
$invoker = Invoker::of('test');
var_dump($invoker->getDescription());

class A {
    function test(array $x) {}
}

$invoker = Invoker::of('A::test');
var_dump($invoker->getDescription());

$invoker = Invoker::of([new A(), 'test']);
var_dump($invoker->getDescription());


?>
--EXPECTF--
string(20) "var_dump(<internal>)"
string(69) "Closure::__invoke($a, array $b, callable $c, php\lang\Invoker $d, $f)"
string(12) "test($x, $y)"
string(17) "A::test(array $x)"
string(17) "A::test(array $x)"
