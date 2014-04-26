--TEST--
Basic regex test - object.find + group
--FILE--
<?php

use php\util\Regex;

$regex = Regex::of('([0-9]+)')->with('1 2 3');

while($regex->find()) {
    var_dump($regex->group());
}

?>
--EXPECT--
string(1) "1"
string(1) "2"
string(1) "3"