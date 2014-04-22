--TEST--
Basic cursor test each
--FILE--
<?php

use php\util\Flow;

$cursor = Flow::of(['a' => 10, 'b' => 20, 'c' => 30, 'd' => 40, 'e' => 50]);
$count = $cursor->each(function($value, $key){
    var_dump($key . '=' . $value);
    if ($key === 'd')
        return false;
});

var_dump('count=' . $count);

?>
--EXPECT--
string(4) "a=10"
string(4) "b=20"
string(4) "c=30"
string(4) "d=40"
string(7) "count=4"
