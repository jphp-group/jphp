--TEST--
Test object typehinting.
--FILE--
<?php

class A {
    static function testReturn(): object {
        return new A();
    }

    function testArgs(object $x) {
        var_dump(get_class($x));
    }
}

$a = A::testReturn();
$a->testArgs($a);
$a->testArgs(new stdClass());
$a->testArgs('string');

?>
--EXPECTF--
string(1) "A"
string(8) "stdClass"

Fatal error: Uncaught TypeError: Argument 1 passed to A::testArgs() must be of the type object, string given, called in %s on line 16, position %d and defined in %s on line 8, position %d
Stack Trace:
#0 {main}
  thrown in %s on line 8