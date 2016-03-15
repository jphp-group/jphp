--TEST--
php\lib\num Test format
--FILE--
<?php

use php\lib\num;

var_dump(num::format(100500900.123456, '###,###', '.', ' '));
var_dump(num::format(100500900.123456, '###,###.000', '.', ' '));

?>
--EXPECT--
string(11) "100 500 900"
string(15) "100 500 900.123"
