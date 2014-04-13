--TEST--
Basic cursor test repetandly
--FILE--
<?php

use php\util\Cursor;

$arr = [1,2];
$cur = Cursor::of($arr);

var_dump($cur->count());

try {
    var_dump($cur->count());
} catch (Exception $e) {
    var_dump($e->getMessage());
}

?>
--EXPECT--
int(2)
string(39) "Unable to iterate the cursor repeatedly"