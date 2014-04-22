--TEST--
Basic cursor test map + reduce
--FILE--
<?php

use php\util\Flow;

$arr = [1,2,3,4];

echo "--simple-map\n";
var_dump(Flow::of($arr)->map(function($e) { return $e * 20; })->toArray());

echo "--simple-reduce\n";
var_dump(Flow::of($arr)->reduce(function($result, $e) { return $result + $e; }));

echo "--simple-map+reduce\n";
var_dump(Flow::of($arr)
    ->map(function($e) { return $e * 20; })
    ->reduce(function($result, $e) { return $result + $e; }));

?>
--EXPECT--
--simple-map
array(4) {
  [0]=>
  int(20)
  [1]=>
  int(40)
  [2]=>
  int(60)
  [3]=>
  int(80)
}
--simple-reduce
int(10)
--simple-map+reduce
int(200)
