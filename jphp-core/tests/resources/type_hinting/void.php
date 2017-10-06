--TEST--
Test void typhinting.
--FILE--
<?php

class A {
    function test(): void {
        echo "test", "\n";
    }

    function test2(): void {
        echo "test2", "\n";
        return;
    }
}

// test inheritance.
class B extends A {
    function test(): void {
        parent::test();
    }
}

$b = new B();
$a = new A();
$a->test();
$b->test();

$a->test2();

?>
--EXPECTF--
test
test
test2
