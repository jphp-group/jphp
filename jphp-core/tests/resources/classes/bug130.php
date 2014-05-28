--TEST--
Bug #130
--FILE--
<?php

$b = new stdClass();
$b->c = new stdClass();
$b->c->d[0] = new stdClass();
$b->c->d[0]->foo = 'stdClass';

$a = new $b->c->d[0]->foo();
var_dump($a);
?>
--EXPECTF--
object(stdClass)#%d (0) {
}
