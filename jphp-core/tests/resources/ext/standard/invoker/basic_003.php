--TEST--
Invoker basic test is* methods
--FILE--
<?php

use php\lang\Invoker;

$invoker = new Invoker(function($a, $b, $c = 20){});
echo("--check-closure\n");
var_dump($invoker->isClosure(), $invoker->isDynamicCall(), $invoker->isNamedFunction(), $invoker->isStaticCall());

$invoker = new Invoker('var_dump');
echo("--check-named-function\n");
var_dump($invoker->isClosure(), $invoker->isDynamicCall(), $invoker->isNamedFunction(), $invoker->isStaticCall());

class A {
    static function foo() {}
    function bar() {}
}

$invoker = new Invoker(['A', 'foo']);
echo("--check-static-call\n");
var_dump($invoker->isClosure(), $invoker->isDynamicCall(), $invoker->isNamedFunction(), $invoker->isStaticCall());

$invoker = new Invoker([new A(), 'bar']);
echo("--check-dynamic-call\n");
var_dump($invoker->isClosure(), $invoker->isDynamicCall(), $invoker->isNamedFunction(), $invoker->isStaticCall());

?>
--EXPECT--
--check-closure
bool(true)
bool(false)
bool(false)
bool(false)
--check-named-function
bool(false)
bool(false)
bool(true)
bool(false)
--check-static-call
bool(false)
bool(false)
bool(false)
bool(true)
--check-dynamic-call
bool(false)
bool(true)
bool(false)
bool(false)
