--TEST--
jmp into a finally block 01
--FILE--
<?php
function foo() {
    goto test;
    try {
    } finally {
        test:
    }
}
?>
--EXPECTF--

Fatal error: 'goto' into loop, switch or finally statement is disallowed in %s on line %d, position %d
