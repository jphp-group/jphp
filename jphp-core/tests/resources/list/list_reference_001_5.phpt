--TEST--
"Reference Unpacking - General" list() - 5
--FILE--
<?php
$arr = array("one" => 1, "two" => array(2));
["one" => &$a, "two" => [&$b], "three" => &$c] = $arr;
var_dump($a, $b, $c);
var_dump($arr);
?>
--EXPECT--
int(1)
int(2)
NULL
array(3) {
  ["one"]=>
  int(1)
  ["two"]=>
  array(1) {
    [0]=>
    int(2)
  }
  ["three"]=>
  NULL
}