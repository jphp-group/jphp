<?php

function test(){
    $b1[6] = 123;
    return $e -= $b1[6];
}

$b1[1] = 123;
$y = $x += $b1[1];

return $y + test() === 0 ? 'success' : 'false';
