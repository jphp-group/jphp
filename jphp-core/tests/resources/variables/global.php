<?php

$x = 'foo';
global $y;
$y = 'bar';

function test(){
    global $x, $y;
    return $x . $y . "|" . $GLOBALS['x'] . $GLOBALS['y'];
}

return test();
