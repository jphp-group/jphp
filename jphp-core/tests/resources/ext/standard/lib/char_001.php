--TEST--
php\lib\str Test - leng
--FILE--
<?php

use php\lib\Char as char;

var_dump(char::of(65));
var_dump(char::ord('A'));

var_dump(char::isLower('a'));
var_dump(char::isLower('aA'));
var_dump(char::isLower(' '));
var_dump(char::isLower(''));

var_dump(char::isUpper('A'));
var_dump(char::isUpper('Aa'));
var_dump(char::isUpper(' '));
var_dump(char::isUpper(''));

var_dump(char::isDigit('1'));
var_dump(char::isDigit('1a'));
var_dump(char::isDigit(' '));
var_dump(char::isDigit(''));

var_dump(char::lower('A'));
var_dump(char::upper('a'));

var_dump(char::number('8'));

?>
--EXPECTF--
string(1) "A"
int(65)
bool(true)
bool(true)
bool(false)
bool(false)
bool(true)
bool(true)
bool(false)
bool(false)
bool(true)
bool(true)
bool(false)
bool(false)
string(1) "a"
string(1) "A"
int(8)