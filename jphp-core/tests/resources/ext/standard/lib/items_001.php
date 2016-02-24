--TEST--
php\lib\items Test - common
--FILE--
<?php

use php\lib\arr as Items;

var_dump(Items::count([1, 2, 3]));
var_dump(Items::flatten([[1], [2], [3]]));
var_dump(Items::keys(['x' => 0, 'y' => 0]));
var_dump(Items::toArray(['x' => 'x', 'y' => 'y']));
var_dump(Items::toArray(['x' => 'x', 'y' => 'y'], true));
var_dump(Items::toList([1, 2], 3, [49]));

?>
--EXPECT--
int(3)
array(3) {
  [0]=>
  int(1)
  [1]=>
  int(2)
  [2]=>
  int(3)
}
array(2) {
  [0]=>
  string(1) "x"
  [1]=>
  string(1) "y"
}
array(2) {
  [0]=>
  string(1) "x"
  [1]=>
  string(1) "y"
}
array(2) {
  ["x"]=>
  string(1) "x"
  ["y"]=>
  string(1) "y"
}
array(4) {
  [0]=>
  int(1)
  [1]=>
  int(2)
  [2]=>
  int(3)
  [3]=>
  int(49)
}