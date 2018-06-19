--TEST--
#268 Negative strings
--FILE--
<?php

$str = "foobar";
var_dump($str[-1]);
var_dump($str[-2]);
var_dump($str[-3]);
var_dump($str[-4]);
var_dump($str[-5]);
var_dump($str[-6]);
var_dump($str[-7]);

?>
--EXPECT--
string(1) "r"
string(1) "a"
string(1) "b"
string(1) "o"
string(1) "o"
string(1) "f"
string(0) ""