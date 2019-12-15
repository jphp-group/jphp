--TEST--
Test error operation of random_bytes()
--FILE--
<?php
//-=-=-=-

try {
    $bytes = random_bytes();
} catch (TypeError $e) {
    echo $e->getMessage().PHP_EOL;
}

try {
    $bytes = random_bytes(0);
} catch (Error $e) {
    echo $e->getMessage().PHP_EOL;
}

?>
--EXPECTF--
Warning: random_bytes() expects at least 1 parameter(s), 0 given in %s on line %d at pos %d
Length must be greater than 0
