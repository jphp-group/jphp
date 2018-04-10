--TEST--
TextWord basic
--FILE--
<?php

use text\TextWord;

$text = new TextWord('hello world');

var_dump($text->length());
var_dump("$text");
var_dump($text->capitalize() . "");
var_dump($text->initials() . "");
var_dump($text->wrap(7, "|") . "");

$text = new TextWord('hELLo WorLd');
var_dump($text->capitalize() . "");
var_dump($text->capitalizeFully() . "");
var_dump($text->swapCase() . "");
var_dump($text->uncapitalize() . "");
var_dump((clone $text) . "");

?>
--EXPECT--
int(11)
string(11) "hello world"
string(11) "Hello World"
string(2) "hw"
string(11) "hello|world"
string(11) "HELLo WorLd"
string(11) "Hello World"
string(11) "HellO wORlD"
string(11) "hELLo worLd"
string(11) "hELLo WorLd"