--TEST--
Class private constant visibility error
--FILE--
<?php
class A {
    private const privateConst = 'privateConst';
}
var_dump(A::privateConst);
?>
--EXPECTF--

Fatal error: Cannot access private constant A::privateConst in %s on line 5, position %d