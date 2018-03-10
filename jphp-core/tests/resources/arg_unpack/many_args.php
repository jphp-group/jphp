--TEST--
Argument unpacking with many arguments
--FILE--
<?php

function fun(...$args) {
    var_dump(count($args));
}

$array = array_fill(0, 10000, 42);
fun(...$array, ...$array);

?>
--EXPECT--
int(20000)
