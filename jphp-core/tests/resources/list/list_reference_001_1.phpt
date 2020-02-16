--TEST--
"Reference Unpacking - General" list()
--FILE--
<?php
$arr = array(1, array(2));
list(&$a, list(&$b)) = $arr;
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