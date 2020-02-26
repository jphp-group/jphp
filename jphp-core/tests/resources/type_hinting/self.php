--TEST--
Test self type hinting.
--FILE--
<?php

trait T {
    function testTrait(self $x): self {
        return $x;
    }

    function testTrait2(?self $x): ?self {
        return $x;
    }
}

class A {
    use T;

    function test(self $x): self {
        return $x;
    }

    function test2(?self $x): ?self {
        return $x;
    }
}

$a = new A();
var_dump($a->test($a));
var_dump($a->test2(null));
var_dump($a->testTrait($a));
var_dump($a->testTrait2(null));

try {
    $a->test(new stdClass());
} catch (TypeError $e) {
    echo $e->getMessage(), "\n\n";
}

try {
    $a->test(123);
} catch (TypeError $e) {
    echo $e->getMessage(), "\n\n";
}

try {
    $a->test2(new stdClass());
} catch (TypeError $e) {
    echo $e->getMessage(), "\n\n";
}

try {
    $a->test2(123);
} catch (TypeError $e) {
    echo $e->getMessage(), "\n\n";
}

?>
--EXPECTF--
object(A)#%d (0) {
}
NULL
object(A)#%d (0) {
}
NULL
Argument 1 passed to A::test() must be an instance of A, instance of stdClass given, called in %s on line %d, position %d and defined

Argument 1 passed to A::test() must be an instance of A, int given, called in %s on line %d, position %d and defined

Argument 1 passed to A::test2() must be an instance of A or null, instance of stdClass given, called in %s on line %d, position %d and defined

Argument 1 passed to A::test2() must be an instance of A or null, int given, called in %s on line %d, position %d and defined