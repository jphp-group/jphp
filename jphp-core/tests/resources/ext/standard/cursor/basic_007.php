--TEST--
Basic cursor test toString
--FILE--
<?php

use php\util\Cursor;

var_dump(Cursor::of(['foo', 'bar'])->toString(','));
var_dump(Cursor::of(['fail', 'fail', 'foo', 'bar', 'fail'])->skip(2)->limit(2)->toString(','));

?>
--EXPECT--
string(7) "foo,bar"
string(7) "foo,bar"