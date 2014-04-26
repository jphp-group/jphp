--TEST--
Basic regex test - quote
--FILE--
<?php

use php\util\Regex;

var_dump(Regex::quote('^foobar$'));
var_dump(Regex::quoteReplacement('$1'));


?>
--EXPECT--
string(12) "\Q^foobar$\E"
string(3) "\$1"