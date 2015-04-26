--TEST--
SharedMap basic test
--FILE--
<?php

use php\util\Flow;
use php\util\SharedMap;

$map = new SharedMap([1, -2]);

var_dump($map->set(1, 2));
var_dump($map->set(2, 3));

var_dump($map->has(1));

var_dump(Flow::of($map)->toArray());

var_dump($map->remove(1));
var_dump($map->count());
var_dump($map->has(1));

var_dump($map->isEmpty());
$map->clear();
var_dump($map->isEmpty());
var_dump(Flow::of($map)->toArray());

?>
--EXPECT--
int(-2)
NULL
bool(true)
array(3) {
  [0]=>
  int(1)
  [1]=>
  int(2)
  [2]=>
  int(3)
}
int(2)
int(2)
bool(false)
bool(false)
bool(true)
array(0) {
}