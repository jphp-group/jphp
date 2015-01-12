--TEST--
Testing array access with numeric key
--FILE--
<?php
$a = [2 => '3'];
$a['2'] = '4';
var_dump($a[2]);
var_dump($a['2']);
?>
--EXPECT--
string(1) "4"
string(1) "4"
