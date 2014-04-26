--TEST--
Basic regex test - split
--FILE--
<?php

use php\util\Regex;
use php\util\RegexException;

var_dump(Regex::split('[0-9]+', 'foo93894bar840'));


try {
    echo "--test-invalid\n";
    Regex::split('[0-9+', '389foo93894bar840');
} catch (RegexException $e) {
    echo "test success exception";
}

?>
--EXPECT--
array(2) {
  [0]=>
  string(3) "foo"
  [1]=>
  string(3) "bar"
}
--test-invalid
test success exception