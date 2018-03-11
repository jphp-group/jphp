--TEST--
JsonProcessor test #3: Objects
--FILE--
<?php

use php\format\JsonProcessor;

$json = new JsonProcessor();
$object = new stdClass();
$object->x = 20;
$object->y = 30;

echo $json->format($object);

?>
--EXPECT--
{"x":20,"y":30}
