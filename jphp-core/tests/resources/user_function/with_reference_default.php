<?php

function test(&$arg1 = 20, &$arg2 = 20){
    return $arg2;
}

$var1 = 100500;
$var2 = test($var1);

return $var1 + $var2; // 100520
