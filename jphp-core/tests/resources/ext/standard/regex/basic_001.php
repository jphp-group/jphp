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
    echo "test success exception";
}

?>
--EXPECT--
Regex::match('^[0-9]+$', '03894') == 1
Regex::match('^[0-9]+$', ' 03894') == 0
--text-invalid
Regex::match('^[0-9+$', '333') == test success exception
