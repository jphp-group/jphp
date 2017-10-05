--TEST--
Invoker basic test canAccess
--FILE--
<?php

use php\lang\Invoker;

$invoker = new Invoker('var_dump');
var_dump($invoker->canAccess());

$invoker = new Invoker(function(){});
var_dump($invoker->canAccess());

class A {
    static function test1() {}
    private static function test2() {}
    static function test3() {
        return new Invoker('A::test2');
    }
}

$invoker = new Invoker('A::test1');
var_dump($invoker->canAccess());

$invoker = A::test3();
var_dump($invoker->canAccess());
$invoker();

$invoker = new Invoker('A::test2');

?>
--EXPECTF--
bool(true)
bool(true)
bool(true)
bool(false)

Fatal error: Uncaught TypeError: Argument 1 passed to php\lang\Invoker::__construct() must be of the type callable, string given, called in %s on line 26, position %d and defined in %s on line %d, position %d
Stack Trace:
#0 {main}
  thrown in %s on line %d