--TEST--
Null Coalesce operator.
https://wiki.php.net/rfc/isset_ternary
--FILE--
<?php

$arr = ['x' => 0, 'y' => 1, 'z' => null, 'a' => '', 'b' => '0'];

$x = $arr['x'] ?? 'x_fail';
$y = $arr['y'] ?? 'y_fail';
$z = $arr['z'] ?? 'z_success';
$a = $arr['a'] ?? 'a_fail';
$b = $arr['b'] ?? 'b_fail';
$c = $arr['c'] ?? 'c_success';

var_dump($a, $y, $z, $a, $b, $c);

?>
--EXPECT--
string(0) ""
int(1)
string(9) "z_success"
string(0) ""
string(1) "0"
string(9) "c_success"