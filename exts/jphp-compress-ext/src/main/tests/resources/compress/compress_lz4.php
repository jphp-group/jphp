--TEST--
Test LZ4 write & read
--FILE--
<?php

use compress\Bzip2InputStream;
use compress\Bzip2OutputStream;
use compress\Lz4InputStream;
use compress\Lz4OutputStream;
use php\io\MemoryStream;

$gz = new Lz4OutputStream($stream = new MemoryStream());
$gz->write("Hello World");
$gz->flush();
$gz->close();

$stream->seek(0);
var_dump($stream->readAll() != "Hello World");

$stream->seek(0);
$gz = new Lz4InputStream($stream);
var_dump($gz->readAll());

?>
--EXPECTF--
bool(true)
string(11) "Hello World"