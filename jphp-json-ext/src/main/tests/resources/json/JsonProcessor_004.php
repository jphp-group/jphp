--TEST--
JsonProcessor test #4: Deserialize
--FILE--
<?php

use php\format\JsonProcessor;

$json = new JsonProcessor();
var_dump($json->parse('1'));
var_dump($json->parse('1.1'));
var_dump($json->parse('true'));
var_dump($json->parse('false'));
var_dump($json->parse('null'));
var_dump($json->parse('"foobar"'));

?>
--EXPECT--
int(1)
float(1.1)
bool(true)
bool(false)
NULL
string(6) "foobar"
