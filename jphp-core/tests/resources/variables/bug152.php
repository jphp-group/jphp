--TEST--
Bug #152: array[var] in magic string
--FILE--
<?php
$a = ["b" => "foobar"];
$b = "b";
echo "$a[$b]";
?>
--EXPECT--
foobar