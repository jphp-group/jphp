<?php

$x = 'foo';
global $y;
$y = 'bar';

function test(){
    global $x, $y;
    return $x . $y . "|" . $GLOBALS['x'] . $GLOBALS['y'];
}

function test2($var) {
    global ${$var}, $y;
    return ${$var} . $y;
}

if (test2('x') !== 'foobar')
    return 'fail_2: var-var';

return test();
