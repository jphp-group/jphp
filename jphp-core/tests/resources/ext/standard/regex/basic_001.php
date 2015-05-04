--TEST--
Basic regex test - match
--FILE--
<?php

use php\lang\IllegalArgumentException;
use php\util\Regex;

echo "Regex::match('^[0-9]+$', '03894') == ", Regex::match('^[0-9]+$', '03894') ? "1" : "0", "\n";
echo "Regex::match('^[0-9]+$', ' 03894') == ", Regex::match('^[0-9]+$', ' 03894') ? "1" : "0", "\n";

echo "--text-invalid\n";

try {
    echo "Regex::match('^[0-9+$', '333') == ", Regex::match('^[0-9+$', '333');
} catch (IllegalArgumentException $e) {
    echo "test success exception\n";
}

var_dump(Regex::match('sss', ' SSs ', Regex::CASE_INSENSITIVE));
var_dump(Regex::match('sss', ' SSs '));
var_dump(Regex::match('sss', ' sss '));
var_dump(Regex::match('^sss$', ' sss '));
var_dump(Regex::match('^sss$', 'sss'));

?>
--EXPECT--
Regex::match('^[0-9]+$', '03894') == 1
Regex::match('^[0-9]+$', ' 03894') == 0
--text-invalid
Regex::match('^[0-9+$', '333') == test success exception
bool(true)
bool(false)
bool(true)
bool(false)
bool(true)