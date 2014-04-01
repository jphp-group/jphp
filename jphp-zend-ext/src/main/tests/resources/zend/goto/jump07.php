--TEST--
jump 07: goto into loop (backward)
--FILE--
<?php
while (0) {
    L1: echo "bug\n";
}
goto L1;
?>
--EXPECTF--

Fatal error: 'goto' into loop, switch or finally statement is disallowed in %s on line 5, position %d
