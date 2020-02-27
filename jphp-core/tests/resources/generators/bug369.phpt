--TEST--
yield can be used without a value
--FILE--
<?php

$a = function() { var_dump((yield)['a']); };
$a()->send(['a' => 'pony']);

?>
--EXPECT--
string(4) "pony"