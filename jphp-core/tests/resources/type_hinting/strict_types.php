--TEST--
Test strict_types
--FILE--
<?php
declare(strict_types=true);

class A {
    function test(float $x) {
        return $x;
    }
}

$a = new A();
var_dump($a->test(1));
var_dump($a->test(1.3));
var_dump($a->test('17'));
?>
--EXPECTF--
float(1)
float(1.3)

Recoverable error: Argument 1 passed to A::test() must be of the type float, string given, called in %s on line 13, position 12 and defined in %s on line 5, position 25