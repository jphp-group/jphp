--TEST--
Flow toArray
--FILE--
<?php

use php\util\Flow;

$arr1 = Flow::of(['x' => 1,2,3])->toArray();
$arr2 = Flow::of(['x' => 100, 'y' => 500])->withKeys()->toArray();

var_dump($arr1, $arr2);

?>
--EXPECTF--
array(3) {
  [0]=>
  int(1)
  [1]=>
  int(2)
  [2]=>
  int(3)
}
array(2) {
  ["x"]=>
  int(100)
  ["y"]=>
  int(500)
}
