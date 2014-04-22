--TEST--
Test php\io\File - simple
--FILE--
<?php

use php\io\File;

$file = new File('/path/to/file');

var_dump($file->getName());
?>
--EXPECT--
string(4) "file"
