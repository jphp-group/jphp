--TEST--
php\lib\num Test convert
--FILE--
<?php

use php\lib\num;

var_dump(num::toBin(100500));
var_dump(num::toHex(100500));
var_dump(num::toOctal(100500));
var_dump(num::toString(100500, 5));

echo "--test-decode\n";

var_dump(num::decode('0x18894'));
var_dump(num::decode('0304224'));

?>
--EXPECT--
string(17) "11000100010010100"
string(5) "18894"
string(6) "304224"
string(8) "11204000"
--test-decode
int(100500)
int(100500)
