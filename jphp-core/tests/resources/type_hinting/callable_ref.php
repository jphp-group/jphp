--TEST--
Test callable type hinting with ref
--FILE--
<?php

function test(callable &$x){

}

$x = 'sin';
test($x);

echo "Done.";
--EXPECT--
Done.
