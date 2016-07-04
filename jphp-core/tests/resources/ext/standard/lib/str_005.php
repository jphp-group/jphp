--TEST--
php\lib\str Test - isLower, isUpper, lower,  upper
--FILE--
<?php

use php\lib\Str as str;
use php\util\Locale;

Locale::setDefault(Locale::RUSSIAN(), true);

var_dump(str::isLower('моя строка'));
var_dump(str::isLower('Моя строка'));
var_dump(str::isLower(''));

var_dump(str::isUpper('МОЯ СТРОКА'));
var_dump(str::isUpper('мОЯ СТРОКА'));
var_dump(str::isUpper(''));

var_dump(str::isUpper(' $ % 467'));
var_dump(str::isLower(' $ % 467'));

var_dump(str::upper('мОя СтрОка'));
var_dump(str::lower('мОя СтрОка'));

?>
--EXPECT--
bool(true)
bool(false)
bool(false)
bool(true)
bool(false)
bool(false)
bool(false)
bool(false)
string(10) "МОЯ СТРОКА"
string(10) "моя строка"