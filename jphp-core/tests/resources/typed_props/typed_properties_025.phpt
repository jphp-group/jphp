--TEST--
Test typed properties type must precede first declaration in group
--FILE--
<?php
class Foo {
    public $bar,
           int $qux;
}
?>
--EXPECTF--

Parse error: Syntax error, unexpected 'int' in %s on line 4, position 12
