--TEST--
Trying to create a constant on Trait
--FILE--
<?php

trait foo {
    const a = 1;
}

?>
--EXPECTF--

Parse error: Syntax error, unexpected 'const' in %s on line %d, position %d