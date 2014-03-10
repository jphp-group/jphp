<?php

function test(){
    $x = 100;
    $y =& $x;
    $z =& $y;
    unset($y);

    return $x == 100 && $z == 100 && $y == NULL ? 'success' : 'fail';
}

return test();
