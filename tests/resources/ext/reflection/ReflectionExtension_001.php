--TEST--
Reflection Extension -> basic
--FILE--
<?php

$r = new ReflectionExtension("core");
var_dump($r->getName());
var_dump($r->getVersion());

try {
    $r = new ReflectionExtension("unknown");
} catch (ReflectionException $e){
    var_dump($e->getMessage());
}
?>
--EXPECTF--
string(4) "Core"
string(%d) "%d.%s
string(32) "Extension unknown does not exist"
