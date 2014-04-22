--TEST--
Test php\io\File - simple
--FILE--
<?php

use php\io\File;

echo "--test-create\n";
$file = File::createTemp('foo', 'bar');
var_dump($file instanceof File);
var_dump($file->exists());
var_dump($file->getPath());

echo "--test-can-write\n";
var_dump($file->canWrite());

echo "--test-can-read\n";
var_dump($file->canRead());

$file->delete();
echo "--test-delete-temp\n";
var_dump($file->exists());

?>
--EXPECTF--
--test-create
bool(true)
bool(true)
string(%d) %s
--test-can-write
bool(true)
--test-can-read
bool(true)
--test-delete-temp
bool(false)
