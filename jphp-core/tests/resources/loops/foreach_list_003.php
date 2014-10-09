--TEST--
foreach with list key
--FILE--
<?php

$array = [['a', 'b'], 'c', 'd'];

foreach($array as list($key) => list(list(), $a)) {
}

?>
--EXPECTF--

Parse error: Syntax error, unexpected '=>', expecting ')' in %s on line 5, position 30