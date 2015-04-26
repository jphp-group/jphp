--TEST--
Test php\io\Stream - test common
--FILE--
<?php

use php\io\Stream;

$memory = Stream::of('php://memory', 'w+');
$memory->write('foobar');
$memory->seek(0);

var_dump($memory->readFully());

?>
--EXPECTF--
string(6) "foobar"
