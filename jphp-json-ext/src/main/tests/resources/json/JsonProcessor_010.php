--TEST--
JsonProcessor test #10: Serialize
--FILE--
<?php

use php\format\JsonProcessor;
use php\lib\items;

$json = new JsonProcessor();

$json->onSerialize('object', function($val) {
    return items::toArray((array)$val);
});

// ignoring int handler
$obj = new stdClass();
$obj->x = 10;
$obj->y = 20;
$obj->obj = new stdClass();

echo ($json->format($obj));
?>
--EXPECTF--
[10,20,{}]
