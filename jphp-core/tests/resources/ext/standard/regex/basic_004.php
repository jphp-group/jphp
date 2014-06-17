--TEST--
Basic regex test - of
--FILE--
<?php

use php\lang\IllegalArgumentException;
use php\util\Regex;

$regex = Regex::of('[0-9]+');
var_dump($regex instanceof Regex);
var_dump($regex === $regex->with('foobar')); // must be false


echo "--test-invalid\n";
try {
    $regex = Regex::of('[0-9+');
} catch (IllegalArgumentException $e) {
    echo "success 1\n";
}

?>
--EXPECT--
bool(true)
bool(false)
--test-invalid
success 1
