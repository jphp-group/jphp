--TEST--
Test array type hinting with default
--FILE--
<?php

function test(array $x = array(1,2,3)){
    return $x;
}

print_r(test());
test(1);

echo "Done.";
--EXPECTF--
Array
(
    [0] => 1
    [1] => 2
    [2] => 3
)

Recoverable error: Argument 1 passed to test() must be of the type array, integer given in %s on line %d, position %d
