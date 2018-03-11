--TEST--
Base64
--FILE--
<?php

$hello = base64_encode("hello world");
var_dump($hello);
var_dump(base64_decode($hello));

?>
--EXPECT--
string(16) "aGVsbG8gd29ybGQ="
string(11) "hello world"
