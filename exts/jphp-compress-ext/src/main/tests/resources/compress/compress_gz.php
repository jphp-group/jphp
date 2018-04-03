--TEST--
Test GZip write & read
--FILE--
<?php

use compress\GzipInputStream;
use compress\GzipOutputStream;
use php\io\MemoryStream;

$gz = new GzipOutputStream($stream = new MemoryStream(), ['comment' => 'foobar', 'compressionLevel' => 9]);
$gz->write("Hello World");
$gz->flush();
$gz->close();

$stream->seek(0);
var_dump($stream->readAll() != "Hello World");

$stream->seek(0);
$gz = new GzipInputStream($stream);
var_dump($gz->readAll());

?>
--EXPECTF--
bool(true)
string(11) "Hello World"