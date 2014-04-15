--TEST--
Test php\io\File - test attrs
--FILE--
<?php

use php\io\File;

$file = File::createTemp('foo', 'bar');

echo "--test-read-write\n";
$file->setReadable(true);
$file->setWritable(true);
var_dump($file->canRead());
var_dump($file->canWrite());


?>
--EXPECTF--
--test-read-write
bool(true)
bool(true)
