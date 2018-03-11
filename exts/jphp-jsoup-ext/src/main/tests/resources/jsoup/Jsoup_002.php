--TEST--
Jsoup test #2
--FILE--
<?php

use php\io\Stream;
use php\jsoup\Jsoup;

$source = Stream::of('php://memory', 'w+');
$source->write('<a>foobar</a>');
$source->seek(0);

$doc = Jsoup::parse($source, 'UTF-8', 'examle.com');

var_dump($doc->select('a')->text());
?>
--EXPECT--
string(6) "foobar"