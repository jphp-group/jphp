--TEST--
Test number typehinting.
--FILE--
<?php

class A {
    function testReturn(): int {
        return 10;
    }

    function testReturn2(): float {
        return 10;
    }

    function testReturn3(int $x): int {
        return $x + 1;
    }

    function testReturn4(int $x): float {
        return $x + 1;
    }

    function testReturn5(int $x): string {
        return $x + 1;
    }

    function testReturn6(string $x): int {
        return $x;
    }
}

$a = new A();
var_dump($a->testReturn());
var_dump($a->testReturn2());

var_dump($a->testReturn3('10'));
var_dump($a->testReturn3(10.2));
var_dump($a->testReturn3(10.7));

var_dump($a->testReturn4(10));

var_dump($a->testReturn5(10));
var_dump($a->testReturn6(222));
?>
--EXPECTF--
int(10)
float(10)
int(11)
int(11)
int(11)
float(11)
string(2) "11"
int(222)