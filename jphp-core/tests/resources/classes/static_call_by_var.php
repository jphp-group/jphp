--TEST--
Static call by var
--FILE--
<?php

function call($class) {
    $class::call();
}

class A {
    static function call() {
        echo "A\n";
    }
}

class B {
    static function call() {
        echo "B\n";
    }
}

call('A');
call('B');
?>
--EXPECT--
A
B