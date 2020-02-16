--TEST--
"Reference Unpacking - General" list() - 4
--FILE--
<?php
$arr = array(1, array(2));
[&$a, [&$b], &$c] = $arr;
var_dump($a, $b, $c);
var_dump($arr);
?>
--EXPECT--
int(1)
int(2)
NULL
array(3) {
  [0]=>
  int(1)
  [1]=>
  array(1) {
    [0]=>
    int(2)
  }
  [2]=>
  NULL
}