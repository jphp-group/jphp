--TEST--
Trying to use instanceof for a method twice
--FILE--
<?php

trait foo {
    public function foo() {
        return 1;
    }
}

trait foo2 {
    public function foo() {
        return 2;
    }
}


class A extends foo {
}

?>
--EXPECTF--

Fatal error: A cannot extend from foo - it is not an class in %s on line %d, position %d