--TEST--
Class protected constant visibility error
--FILE--
<?php
class A {
    protected const protectedConst = 'protectedConst';
}
var_dump(A::protectedConst);
?>
--EXPECTF--

Fatal error: Cannot access protected constant A::protectedConst in %s on line 5, position %d