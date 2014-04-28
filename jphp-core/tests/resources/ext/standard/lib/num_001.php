--TEST--
php\lib\num Test compare
--FILE--
<?php

use php\lib\num;

var_dump(num::compare(3.1, 3));
var_dump(num::compare(3, 3));
var_dump(num::compare(2, 3.1));

echo "--test-strings\n";
var_dump(num::compare('10', 9));
var_dump(num::compare('10', '05'));
var_dump(num::compare('10', '10'));
var_dump(num::compare('3.0', '10'));

?>
--EXPECT--
int(1)
int(0)
int(-1)
--test-strings
int(1)
int(1)
int(0)
int(-1)
