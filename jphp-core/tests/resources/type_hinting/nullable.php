--TEST--
Test nullable ?
--FILE--
<?php

class A {
    function testArg(?stdClass $x) {
        var_dump($x);
    }

    function testReturn($x): ?stdClass {
        return $x;
    }
}

class B extends A {
    function testArg(?stdClass $x) {
    }
}

$a = new A();
$a->testArg(new stdClass());
$a->testArg(null);

var_dump($a->testReturn(null));
var_dump($a->testReturn(new stdClass()));

$a->testArg(false);

?>
--EXPECTF--
object(stdClass)#%d (0) {
}
NULL
NULL
object(stdClass)#%d (0) {
}

Fatal error: Uncaught TypeError: Argument 1 passed to A::testArg() must be an instance of stdClass or null, bool given, called in %s on line 25, position %d and defined in %s on line 4, position %d
Stack Trace:
#0 {main}
  thrown in %s on line 4