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
?>
--EXPECTF--
Array
(
    [0] => 1
    [1] => 2
    [2] => 3
)

Fatal error: Uncaught TypeError: Argument 1 passed to test() must be of the type array, int given, called in %s on line 8, position %d and defined in %s on line 3, position %d
Stack Trace:
#0 {main}
  thrown in %s on line 3