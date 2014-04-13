--TEST--
Cursor toArray
--FILE--
<?php

use php\util\Cursor;

$arr1 = Cursor::of(['x' => 1,2,3])->toArray();
$arr2 = Cursor::of(['x' => 100, 'y' => 500])->toArray(true);

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
