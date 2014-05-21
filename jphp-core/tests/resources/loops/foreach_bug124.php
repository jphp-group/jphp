--TEST--
Bug #124
--FILE--
<?php

$a = ['foobar'];
foreach (true ? $a : [$a] as $b) {
    var_dump($b);
}

?>
--EXPECT--
string(6) "foobar"
