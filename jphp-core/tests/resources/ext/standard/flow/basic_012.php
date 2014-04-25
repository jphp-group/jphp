--TEST--
Basic cursor test with stream
--FILE--
<?php

use php\io\MemoryStream;
use php\util\Flow;

$buf = new MemoryStream();
$buf->write('foobar');
$buf->seek(0);

$cur = Flow::ofStream($buf);
var_dump($cur->toString(','));

$buf->seek(0);

$cur = Flow::ofStream($buf, 3);
var_dump($cur->toString(','));

$buf->seek(2);
$cur = Flow::ofStream($buf, 2);
var_dump($cur->toString(','));

?>
--EXPECT--
string(11) "f,o,o,b,a,r"
string(7) "foo,bar"
string(5) "ob,ar"
