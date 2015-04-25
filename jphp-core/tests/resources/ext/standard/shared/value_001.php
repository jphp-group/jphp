--TEST--
SharedValue basic test
--FILE--
<?php

use php\util\SharedValue;

$value = new SharedValue();

var_dump($value->isEmpty());
var_dump($value->get());

$value->set('foobar');
var_dump($value->get());

var_dump($value->isEmpty());

$value->set('foobar2', false);
var_dump($value->get());

var_dump($value->set('foobar2'));

$value->remove();

var_dump($value->isEmpty());
var_dump($value->get());

?>
--EXPECT--
bool(true)
NULL
string(6) "foobar"
bool(false)
string(6) "foobar"
string(6) "foobar"
bool(true)
NULL