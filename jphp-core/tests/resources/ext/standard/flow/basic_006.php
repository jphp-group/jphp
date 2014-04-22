--TEST--
Basic cursor test limit and offset + count;
--FILE--
<?php

use php\util\Flow;

$cursor = Flow::ofRange(1, 99);
var_dump('count_with_skip='.$cursor->skip(19)->count());

$cursor = Flow::ofRange(1, 99);
var_dump('count_with_limit='.$cursor->limit(20)->count());

$cursor = Flow::ofRange(1, 99);
var_dump('count_with_skip+limit='.$cursor->skip(20)->limit(40)->count());

echo "--\n";
$cursor = Flow::of([10, 20, 30, 40, 50])->skip(1)->limit(3)->each(function($el){
    var_dump($el);
})

?>
--EXPECT--
string(18) "count_with_skip=80"
string(19) "count_with_limit=20"
string(24) "count_with_skip+limit=40"
--
int(20)
int(30)
int(40)
