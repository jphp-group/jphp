--TEST--
Basic cursor test sort by keys
--FILE--
<?php

use php\util\Flow;

echo "--test-sort-by-keys\n";
$arr = [3 => 1, 1 => 2, 2 => 3];
var_dump(Flow::of($arr)->sortByKeys());

echo "--test-sort-by-keys-with-keys\n";
var_dump(Flow::of($arr)->withKeys()->sortByKeys());

echo "--test-sort-by-keys-with-comparator\n";
var_dump(Flow::of($arr)->withKeys()->sortByKeys(function($key1, $key2){
    if ($key1 == $key2) return 0;
    return $key1 > $key2 ? -1 : 1;
}));

?>
--EXPECT--
--test-sort-by-keys
array(3) {
  [0]=>
  int(2)
  [1]=>
  int(3)
  [2]=>
  int(1)
}
--test-sort-by-keys-with-keys
array(3) {
  [1]=>
  int(2)
  [2]=>
  int(3)
  [3]=>
  int(1)
}
--test-sort-by-keys-with-comparator
array(3) {
  [3]=>
  int(1)
  [2]=>
  int(3)
  [1]=>
  int(2)
}
