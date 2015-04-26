--TEST--
SharedQueue basic test
--FILE--
<?php

use php\util\Flow;
use php\util\SharedMap;
use php\util\SharedQueue;
use php\util\SharedStack;

$queue = new SharedQueue([1, 2, 3]);

var_dump($queue->count());
var_dump($queue->isEmpty());

var_dump($queue->poll());
var_dump($queue->peek());

var_dump(Flow::of($queue)->toArray());

$queue->add(4);

var_dump($queue->peek());

var_dump($queue->poll());
var_dump($queue->poll());

var_dump($queue->count());
var_dump($queue->isEmpty());

var_dump($queue->poll());
var_dump($queue->poll());
$queue->clear();

var_dump($queue->count());
var_dump($queue->isEmpty());

?>
--EXPECT--
int(3)
bool(false)
int(1)
int(2)
array(2) {
  [0]=>
  int(2)
  [1]=>
  int(3)
}
int(2)
int(2)
int(3)
int(1)
bool(false)
int(4)
NULL
int(0)
bool(true)