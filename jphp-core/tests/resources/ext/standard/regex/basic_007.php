--TEST--
Basic regex test - object.foreach
--FILE--
<?php

use php\util\Regex;

$regex = Regex::of('([0-9]+)');
foreach($regex->with('1 2 3') as $value) {
    var_dump($value);
}
?>
--EXPECT--
string(1) "1"
string(1) "2"
string(1) "3"