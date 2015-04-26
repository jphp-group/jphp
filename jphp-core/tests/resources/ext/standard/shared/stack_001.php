--TEST--
SharedStack basic test
--FILE--
<?php

use php\util\Flow;
use php\util\SharedMap;
use php\util\SharedStack;

$stack = new SharedStack([1, -2]);

var_dump($stack->isEmpty());
var_dump($stack->count());

var_dump($stack->push(3));
var_dump($stack->pop());

var_dump($stack->peek());

var_dump($stack->pop());
var_dump($stack->pop());
var_dump($stack->pop());

var_dump($stack->isEmpty());
var_dump($stack->count());

$stack->push('foo');
$stack->push('bar');
var_dump(Flow::of($stack)->toArray());

$stack->clear();
var_dump($stack->isEmpty());
var_dump($stack->count());

?>
--EXPECT--
bool(false)
int(2)
int(3)
int(3)
int(-2)
int(-2)
int(1)
NULL
bool(true)
int(0)
array(2) {
  [0]=>
  string(3) "foo"
  [1]=>
  string(3) "bar"
}
bool(true)
int(0)