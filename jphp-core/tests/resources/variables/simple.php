<?php
$x = 10;
$y = 20;

function test(){
    $x = 40;
    $y = 50;
    return $y - $x;
}

return $x + test() + $y; // 10 + 10 + 20 = 40
