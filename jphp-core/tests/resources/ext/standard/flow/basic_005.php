--TEST--
Basic cursor test current and key
--FILE--
<?php

use php\util\Flow;

$cursor = Flow::of(['x' => 1, 'y' => 2]);

var_dump($cursor->current());
var_dump($cursor->key());

$cursor->next();

var_dump($cursor->current());
var_dump($cursor->key());


?>
--EXPECT--
int(1)
string(1) "x"
int(2)
string(1) "y"
