--TEST--
testing the behavior of string offset chaining
--FILE--
<?php
$array = array('expected_array' => "foobar");
var_dump(isset($array['expected_array']));
var_dump($array['expected_array']);
//var_dump(isset($array['expected_array']['foo']));
var_dump($array['expected_array']['foo']);
//var_dump(isset($array['expected_array']['foo']['bar']));
var_dump($array['expected_array']['foo']['bar']);
?>
--EXPECTF--
bool(true)
string(6) "foobar"
Warning: Illegal string offset 'foo' in %s on line %d at pos %d
string(1) "f"
Warning: Illegal string offset 'foo' in %s on line %d at pos %d
Warning: Illegal string offset 'bar' in %s on line %d at pos %d
string(1) "f"
