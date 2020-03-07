--TEST--
Test static closures
--FILE--
<?php

class A {
    function test() {
        $f = static function() {
            var_dump($this);
        };
        $f();
    }
}

$a = new A();
$a->test();

--EXPECTF--

Fatal error: Using $this when not in object context in %s on line 6, position %d