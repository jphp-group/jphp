--TEST--
foreach with empty list
--FILE--
<?php

$array = [['a', 'b'], 'c', 'd'];

foreach($array as $key => list()) {
}

?>
--EXPECTF--

Fatal error: Cannot use empty list in %s on line 5, position 26
