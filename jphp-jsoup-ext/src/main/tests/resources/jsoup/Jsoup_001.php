--TEST--
Jsoup test #1
--FILE--
<?php

use php\jsoup\Jsoup;

$doc = Jsoup::parseText('<a href="www" id="100500">foobar</a>');
$element = $doc->select('a')->first();

var_dump($element->text());
var_dump($element->tagName());
var_dump($element->nodeName());
var_dump($element->attr('href'));

$element->attr('href', 'www_modified');
var_dump($element->attr('href'));

var_dump($element->html());
var_dump($element->id());

?>
--EXPECT--
string(6) "foobar"
string(1) "a"
string(1) "a"
string(3) "www"
string(12) "www_modified"
string(6) "foobar"
string(6) "100500"