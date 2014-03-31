--TEST--
jump 09: goto into switch (backward)
--FILE--
<?php
switch (0) {
    case 1:
        L1: echo "bug\n";
        break;
}
goto L1;
?>
--EXPECTF--

Fatal error: 'goto' into loop, switch or finally statement is disallowed in %s on line 7, position %d
