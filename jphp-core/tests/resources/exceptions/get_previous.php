--TEST--
Get Previous
--FILE--
<?php

$e1 = new Exception("foo");
$e2 = new Exception("bar", 0, $e1);

var_dump($e1->getPrevious());
var_dump($e2->getPrevious()->getMessage());

?>
--EXPECT--
NULL
string(3) "foo"