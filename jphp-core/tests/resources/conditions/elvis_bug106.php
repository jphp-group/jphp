--TEST--
https://github.com/jphp-compiler/jphp/issues/106
--FILE--
<?php

$a = [];
$b = 0;
$a[$b ? "foo" : "bar"] = "baz";
var_dump($a);
?>
--EXPECT--
array(1) {
  ["bar"]=>
  string(3) "baz"
}
