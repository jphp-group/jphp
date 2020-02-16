--TEST--
"Reference Unpacking - General" list() - 2
--FILE--
<?php
$arr = array(1, array(2));
list($a, &$b) = $arr;
var_dump($arr);
?>
--EXPECT--
array(2) {
  [0]=>
  int(1)
  [1]=>
  array(1) {
    [0]=>
    int(2)
  }
}