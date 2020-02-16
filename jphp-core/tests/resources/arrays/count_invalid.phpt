--TEST--
Only arrays and countable objects can be counted
--FILE--
<?php
$v = null;
$result = count($v);
var_dump($result);

$v = "string";
$result = count($v);
var_dump($result);

$v = 123;
$result = count($v);
var_dump($result);

$v = true;
$result = count($v);
var_dump($result);

$v = false;
$result = count($v);
var_dump($result);

$result = count((object) []);
var_dump($result);

?>
--EXPECTF--
Warning: count(): Parameter must be an array or an object that implements Countable in %s on line %d at pos %d
int(0)
Warning: count(): Parameter must be an array or an object that implements Countable in %s on line %d at pos %d
int(1)
Warning: count(): Parameter must be an array or an object that implements Countable in %s on line %d at pos %d
int(1)
Warning: count(): Parameter must be an array or an object that implements Countable in %s on line %d at pos %d
int(1)
Warning: count(): Parameter must be an array or an object that implements Countable in %s on line %d at pos %d
int(1)
Warning: count(): Parameter must be an array or an object that implements Countable in %s on line %d at pos %d
int(1)