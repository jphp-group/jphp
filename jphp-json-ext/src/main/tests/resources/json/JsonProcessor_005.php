--TEST--
JsonProcessor test #5: Deserialize
--FILE--
<?php

use php\format\JsonProcessor;

$json = new JsonProcessor();
var_dump($json->parse('[1,1.1, true, false, null, "foobar"]'));

?>
--EXPECT--
array(6) {
  [0]=>
  int(1)
  [1]=>
  float(1.1)
  [2]=>
  bool(true)
  [3]=>
  bool(false)
  [4]=>
  NULL
  [5]=>
  string(6) "foobar"
}
