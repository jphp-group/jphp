<?php

function test1(){
    $x = 20;
    $y = 30;
    $z = 10;

    $func1 = function() use (&$x, &$y, $z){
        $x = 40;
        $y = 50;
        $z = 60;
    };
    $func1();

    return $x + $y + $z;
}

$x = 20;
$y = 30;
$func2 = function() use (&$x, $y){
    $x = 50;
    $y = 50;
};

if (($r = test1()) !== 100)
    return "fail_1: $r != 100";

$func2();
if (($r = $x + $y) !== 80)
    return "fail_2: $r != 80";

return 'success';
