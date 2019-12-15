--TEST--
Test error operation of random_int()
--FILE--
<?php
//-=-=-=-

try {
    $randomInt = random_int();
} catch (TypeError $e) {
    echo $e->getMessage().PHP_EOL;
}

try {
    $randomInt = random_int(42);
} catch (TypeError $e) {
    echo $e->getMessage().PHP_EOL;
}

try {
    $randomInt = random_int(42,0);
} catch (Error $e) {
    echo $e->getMessage().PHP_EOL;
}

?>
--EXPECTF--
Warning: random_int() expects at least 2 parameter(s), 0 given in %s on line %d at pos %d
Warning: random_int() expects at least 2 parameter(s), 1 given in %s on line %d at pos %d
Minimum value must be less than or equal to the maximum value
