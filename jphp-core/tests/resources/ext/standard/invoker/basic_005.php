--TEST--
Invoker basic test of method
--FILE--
<?php

use php\lang\Invoker;

$invoker = Invoker::of('unknown');
var_dump($invoker);

$invoker = Invoker::of('var_dump');
var_dump($invoker);

class A {
    private static function test1() { echo "success\n"; }
    public static function test2(Invoker $invoker){
        $invoker();
    }
}

$invoker = Invoker::of('A::test1');
var_dump($invoker);
var_dump("can_access=".($invoker->canAccess() ? 'true' : 'false'));

A::test2($invoker);
?>
--EXPECTF--
NULL
object(php\lang\Invoker)#%d (0) {
}
object(php\lang\Invoker)#%d (0) {
}
string(16) "can_access=false"
success
