--TEST--
JsonProcessor test #6: Deserialize
--FILE--
<?php

use php\format\JsonProcessor;

$json = new JsonProcessor();
var_dump($json->parse('{"x": 100, "y": 500}'));

?>
--EXPECTF--
object(stdClass)#%d (2) {
  ["x"]=>
  int(100)
  ["y"]=>
  int(500)
}
