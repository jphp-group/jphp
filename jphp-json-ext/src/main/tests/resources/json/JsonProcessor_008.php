--TEST--
JsonProcessor test #8: Serialize
--FILE--
<?php

use php\format\JsonProcessor;

$json = new JsonProcessor();
$json->onSerialize('int', function($val){
    return "int:" . $val;
});

echo ($json->format([1, 2])), "\n";

$json->onSerialize('int', null);

echo ($json->format([1, 2])), "\n";
?>
--EXPECTF--
["int:1","int:2"]
[1,2]
