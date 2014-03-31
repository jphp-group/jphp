--TEST--
jmp into a finally block 02
--FILE--
<?php
function foo() {
    try {
        goto test;
    } finally {
        test:
    }
}
?>
--EXPECTF--

Fatal error: 'goto' into loop, switch or finally statement is disallowed in %s on line %d, position %d
