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

    function test3(): void {
        echo "test3", "\n";
        return null;
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
$a->test3();

?>
--EXPECTF--
test
test
test2
test3

Recoverable error: Return value of A::test3() must be the type void, NULL returned in %s on line 32, position 3
