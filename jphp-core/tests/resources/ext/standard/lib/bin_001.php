--TEST--
php\lib\bin Test common
--FILE--
<?php

use php\lib\Binary as bin;
use php\lib\Char;

$bin = bin::of([65, 66, 67, 68]);

var_dump($bin);
var_dump(Char::ord($bin[0]));

$bin2 = 'ABCD';
var_dump(Char::ord($bin2[0]));

var_dump($bin . '-' . $bin2);

?>
--EXPECT--
string(4) "ABCD"
int(65)
int(65)
string(9) "ABCD-ABCD"