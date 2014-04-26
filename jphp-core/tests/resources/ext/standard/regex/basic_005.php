--TEST--
Basic regex test - object.matches
--FILE--
<?php

use php\util\Regex;
use php\util\RegexException;

$regex = Regex::of('[0-9]+');
var_dump($regex->with('100500')->matches());
var_dump($regex->with('  100500 ')->matches());
var_dump($regex->with('a100500')->matches());

?>
--EXPECT--
bool(true)
bool(false)
bool(false)