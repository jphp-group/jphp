--TEST--
Test typed properties delay type check on ast
--FILE--
<?php
class Foo {
    public int $bar = BAR::BAZ * 2;
}

$foo = new Foo();
?>
--EXPECTF--

Compile error: Expecting constant value for Foo::$bar in %s on line 3, position %d