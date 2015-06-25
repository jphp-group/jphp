--TEST--
php\lib\str Test - leng
--FILE--
<?php

use php\lib\Str as str;

$random = str::random();
var_dump($random);

$random = str::random(8);
var_dump($random);

var_dump(str::random() != str::random());

$random = str::random(8, '1234567890');

var_dump($random);
var_dump(str::isNumber($random));

?>
--EXPECTF--
string(16) "%s
string(8) "%s
bool(true)
string(8) "%s
bool(true)