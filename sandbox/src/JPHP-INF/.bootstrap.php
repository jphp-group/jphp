<?php

function test($x) {
    $any = $x;
    return $x + 1;
}

$x = 20;
$y = 40;

$foobar = 'Моя работа';

$a = test($x);

$z = $x + $y;

echo $z;