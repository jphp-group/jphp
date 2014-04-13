--TEST--
Invoker basic test getArgumentCount()
--FILE--
<?php

use php\lang\Invoker;

$invoker = new Invoker(function($a, $b, $c = 20){});
var_dump($invoker->getArgumentCount());


function test($a, $b) {}

$invoker = new Invoker('test');
var_dump($invoker->getArgumentCount());


class foobar {
    function test($a, $b, $c) { }
}
$invoker = new Invoker(['foobar', 'test']);
var_dump($invoker->getArgumentCount());


?>
--EXPECT--
int(3)
int(2)
int(3)