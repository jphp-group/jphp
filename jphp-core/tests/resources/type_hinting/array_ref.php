--TEST--
Test array type hinting with ref
--FILE--
<?php

function test(array &$x){

}

$arr = [];
test($arr, 1);

echo "Done.";
--EXPECTF--
Done.
