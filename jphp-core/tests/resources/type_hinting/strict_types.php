--TEST--
Test strict_types
--FILE--
<?php
declare(strict_types=1);

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

Fatal error: Uncaught TypeError: Argument 1 passed to A::test() must be of the type float, string given, called in %s on line 13, position %d and defined in %s on line 5, position %d
Stack Trace:
#0 {main}
  thrown in %s on line 5