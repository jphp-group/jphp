<?php

$x = 'foo';

function test(){
    global $x, $y;
    return $x . $y;
}

return test();
