--TEST--
Basic regex test - object.reset
--FILE--
<?php

use php\util\Regex;

$regex = Regex::of('[0-9]+')->with('1 2 3');
var_dump($regex->matches());

$regex->reset('123');
var_dump($regex->matches());

?>
--EXPECT--
bool(false)
bool(true)