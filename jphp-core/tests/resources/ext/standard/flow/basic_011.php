--TEST--
Basic cursor test repetandly
--FILE--
<?php

use php\util\Flow;

$arr = [1,2];
$cur = Flow::of($arr);

var_dump($cur->count());

try {
    var_dump($cur->count());
} catch (Exception $e) {
    var_dump($e->getMessage());
}

?>
--EXPECT--
int(2)
string(37) "Unable to iterate the flow repeatedly"
