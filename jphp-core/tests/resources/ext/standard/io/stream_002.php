--TEST--
Test php\io\Stream - test file stream
--FILE--
<?php

use php\io\FileStream;
use php\io\Stream;

$memory = Stream::of(__DIR__ . '/stream_002.inc.txt');
var_dump($memory->readFully());
$memory->close();

var_dump(Stream::getContents(__DIR__ . '/stream_002.inc.txt'));

$memory = Stream::of('file://' . __DIR__ . '/stream_002.inc.txt');
var_dump($memory->read(3));
$memory->close();

$memory = new FileStream(__DIR__ . '/stream_002.inc.txt');
var_dump($memory->readFully());
$memory->close();

try {
    $memory->read(10);
} catch (\php\io\IOException $e) {
    var_dump($e->getMessage());
}

?>
--EXPECTF--
string(6) "foobar"
string(6) "foobar"
string(3) "foo"
string(6) "foobar"
string(13) "Stream Closed"