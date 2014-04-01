--TEST--
jmp into a finally block 03
--FILE--
<?php
function foo() {
    try {
    } finally {
        test:
    }
	goto test;
}
?>
--EXPECTF--

Fatal error: 'goto' into loop, switch or finally statement is disallowed in %s on line %d, position %d
