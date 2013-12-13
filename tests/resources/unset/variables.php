<?php

$x = 'foobar';
unset($x);

function test($arg){
    $y = $arg;
    unset($y);
    return $y;
}

return $x . test('foobar') . 'success';
