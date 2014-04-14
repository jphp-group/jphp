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

?>
--EXPECTF--
bool(true)
bool(true)
bool(true)
bool(false)

Fatal error: Call to private method A::test2() from context '' in %s on line 24, position %d
