--TEST--
Testing GOTO inside finally
--FILE--
<?php

goto A;

try {
    return true;
} finally {
    A: echo "bug";
}

?>
--EXPECTF--

Fatal error: 'goto' into loop, switch or finally statement is disallowed in %s on line 3, position %d
