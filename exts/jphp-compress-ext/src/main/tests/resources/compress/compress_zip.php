--TEST--
Test Zip Archive read
--FILE--
<?php

use compress\ZipArchive;
use php\io\Stream;
use php\lib\arr;

$archive = new ZipArchive(__DIR__ . "/foobar.zip");
print_r(arr::keys($archive->readAll()));

echo "\nread():\n";
$archive->read('test.txt', function ($entry, ?Stream $stream) {
    var_dump($stream->readAll());
});

echo "\nreadAll():\n";
$archive->readAll(function (\compress\ZipArchiveEntry $entry, ?Stream $stream) {
    var_dump($entry->name, $stream ? $stream->readAll() : null);
});

?>
--EXPECTF--
Array
(
    [0] => foobar/
    [1] => foobar/foobar.txt
    [2] => test.txt
)

read():
string(14) "TestContent123"

readAll():
string(7) "foobar/"
NULL
string(17) "foobar/foobar.txt"
string(9) "Foobar123"
string(8) "test.txt"
string(14) "TestContent123"