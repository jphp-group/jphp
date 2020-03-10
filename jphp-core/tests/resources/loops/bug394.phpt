--TEST--
Broken array expansion in foreach
--FILE--
<?php

foreach ([['a', 'b'], ['c', 'd'], ['x', 'y']] as $index => [$name, $value]) {
    var_dump($index, $name, $value);
}
?>
--EXPECTF--
int(0)
string(1) "a"
string(1) "b"
int(1)
string(1) "c"
string(1) "d"
int(2)
string(1) "x"
string(1) "y"