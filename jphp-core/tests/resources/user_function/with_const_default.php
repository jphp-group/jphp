<?php

define('MY_CONST', 10);
const ABC = 10;

function test($x = MY_CONST, $y = ABC){
    return $x + $y;
}

if (($x = test()) !== 20)
    return 'fail_1: ' . $x;

if (test(12) !== 22)
    return 'fail_2';

class FOO {
    const MY_CONST = 20;
}

function test2($x = FOO::MY_CONST){
    return $x;
}

if (test2() !== 20)
    return 'fail_3: class::const';

if (test2(22) !== 22)
    return 'fail_4';

return 'success';