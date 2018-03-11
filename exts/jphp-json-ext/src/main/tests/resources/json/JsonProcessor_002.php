--TEST--
JsonProcessor test #2
--FILE--
<?php

use php\format\JsonProcessor;

$json = new JsonProcessor();
var_dump($json->format([1, 1.1, true, false, null, 'foobar']));

$json = new JsonProcessor(JsonProcessor::SERIALIZE_PRETTY_PRINT);
echo ($json->format([1, 1.1, true, false, null, 'foobar']));

?>
--EXPECT--
string(32) "[1,1.1,true,false,null,"foobar"]"
[
  1,
  1.1,
  true,
  false,
  null,
  "foobar"
]
