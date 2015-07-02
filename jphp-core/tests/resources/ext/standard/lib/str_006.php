--TEST--
php\lib\str Test - isNumber
--FILE--
<?php

use php\lib\Str as str;

var_dump(str::isNumber('012738'));
var_dump(str::isNumber('9999999999999999999999999999999999999999999999'));
var_dump(str::isNumber('9999999999999999999999999999999999999999999999', false));
var_dump(str::isNumber('0.12738'));
var_dump(str::isNumber('0,12738'));
var_dump(str::isNumber('1234 '));
var_dump(str::isNumber(' 1234'));
var_dump(str::isNumber('0x16'));

?>
--EXPECT--
bool(true)
bool(true)
bool(false)
bool(false)
bool(false)
bool(false)
bool(false)
bool(false)