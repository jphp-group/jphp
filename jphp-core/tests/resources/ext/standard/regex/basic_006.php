--TEST--
Basic regex test - object.replace (+first)
--FILE--
<?php

use php\util\Regex;
use php\util\RegexException;

$regex = Regex::of('([0-9]+)');
var_dump($regex->with('1 2 3')->replace('|$1|'));
var_dump($regex->with('1 2 3')->replaceFirst('|$1|'));

$tmp = $regex->with('1 2 3');
var_dump($tmp->replaceWithCallback(function(Regex $r) use ($tmp) {
    if ($tmp !== $r)
        throw new Exception();

    return '|$1|';
}));

?>
--EXPECT--
string(11) "|1| |2| |3|"
string(7) "|1| 2 3"
string(11) "|1| |2| |3|"