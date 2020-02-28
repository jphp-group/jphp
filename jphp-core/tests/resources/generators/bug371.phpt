--TEST--
Test yield syntax in ternary operator
--FILE--
<?php

function a() {
    true ? yield : yield;
    true ?: yield;
    true ?? yield;
}

echo "OK";

?>
--EXPECT--
OK