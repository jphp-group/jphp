--TEST--
JsonProcessor test #1
--FILE--
<?php

use php\format\JsonProcessor;

$json = new JsonProcessor();
var_dump($json->format(1));
var_dump($json->format(1.1));
var_dump($json->format(true));
var_dump($json->format(false));
var_dump($json->format(null));
var_dump($json->format('foobar'));

?>
--EXPECT--
string(1) "1"
string(3) "1.1"
string(4) "true"
string(5) "false"
string(4) "null"
string(8) ""foobar""
