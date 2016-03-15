--TEST--
https://github.com/jphp-compiler/jphp/issues/217
--FILE--
<?php
$array = json_decode(json_encode(['k1' => 1, 'k2' => 2, 'k3' => 3]), true);

$clone = $array;

unset($clone['k1']);

var_dump($array);

?>
--EXPECTF--
array(3) {
  ["k1"]=>
  int(1)
  ["k2"]=>
  int(2)
  ["k3"]=>
  int(3)
}