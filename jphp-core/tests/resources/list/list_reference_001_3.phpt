--TEST--
"Reference Unpacking - General" list() - 3
--FILE--
<?php
$arr = array(1, array(2));
[&$a, [&$b]] = $arr;
var_dump($a, $b);
var_dump($arr);
?>
--EXPECT--
int(1)
int(2)
array(2) {
  [0]=>
  int(1)
  [1]=>
  array(1) {
    [0]=>
    int(2)
  }
}