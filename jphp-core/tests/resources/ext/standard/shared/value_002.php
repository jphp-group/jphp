--TEST--
SharedValue basic test callbacks
--FILE--
<?php

use php\util\SharedValue;

$value = new SharedValue(1);

var_dump($value->get());

var_dump($value->getAndSet(function ($v) { return $v + 1; }));
var_dump($value->get());

var_dump($value->setAndGet(function ($v) { return $v + 1; }));
var_dump($value->get());

?>
--EXPECT--
int(1)
int(1)
int(2)
int(3)
int(3)