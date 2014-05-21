--TEST--
Big #129
--FILE--
<?php

$a = 1;

function isOne($b) {
    return $b === 1;
}

switch($a) {
    case $a > 0 || isOne($a):
        echo 1;
        break;
    default:
        echo 0;
}
?>
--EXPECT--
1
