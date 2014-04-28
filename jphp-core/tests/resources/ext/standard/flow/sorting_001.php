--TEST--
Basic cursor test sort
--FILE--
<?php

use php\util\Flow;

echo "--test-sort\n";
$arr = [3, 1, 5, 2, 7, '0'];
var_dump(Flow::of($arr)->sort());

echo "--test-sort-with-keys\n";
$arr = ['x' => 3, 'y' => 1, 'z' => 2];
var_dump(Flow::of($arr)->withKeys()->sort());

echo "--test-sort-with-comparator\n";
$arr = [3, 1, 2];
var_dump(Flow::of($arr)->sort(function($o1, $o2){
    if ($o1 == $o2) return 0;
    return $o1 > $o2 ? -1 : 1;
}))

?>
--EXPECT--
--test-sort
array(6) {
  [0]=>
  string(1) "0"
  [1]=>
  int(1)
  [2]=>
  int(2)
  [3]=>
  int(3)
  [4]=>
  int(5)
  [5]=>
  int(7)
}
--test-sort-with-keys
array(3) {
  ["y"]=>
  int(1)
  ["z"]=>
  int(2)
  ["x"]=>
  int(3)
}
--test-sort-with-comparator
array(3) {
  [0]=>
  int(3)
  [1]=>
  int(2)
  [2]=>
  int(1)
}
