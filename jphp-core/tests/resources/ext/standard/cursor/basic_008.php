--TEST--
Basic cursor test find
--FILE--
<?php

use php\util\Cursor;

$arr = [1,2,3,4];
$filter = function($e) { return $e % 2 == 0; };

foreach(Cursor::of($arr)->find($filter) as $el) {
    var_dump($el);
}

echo "--with-skip\n";
foreach(Cursor::of($arr)->skip(2)->find($filter) as $el) {
    var_dump($el);
}


?>
--EXPECT--
int(2)
int(4)
--with-skip
int(4)