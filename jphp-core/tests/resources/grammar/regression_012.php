--TEST--
Testing for regression on const list syntax and arrays
--FILE--
<?php

class A {
    const A = [1, FOREACH];
}

?>
--EXPECTF--

Parse error: Syntax error, unexpected 'FOREACH' in %s on line 4, position %d