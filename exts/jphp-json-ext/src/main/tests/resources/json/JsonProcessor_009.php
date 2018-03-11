--TEST--
JsonProcessor test #9: Serialize
--FILE--
<?php

use php\format\JsonProcessor;

$json = new JsonProcessor();
$json->onSerialize('int', function($val){
    return "int:" . $val;
});

$json->onSerialize('array', function($val) {
    return $val;
});

// ignoring int handler
echo ($json->format([1, 2]));
?>
--EXPECTF--
[1,2]
