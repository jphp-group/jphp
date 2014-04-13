<?php

$x = 20;
$y = 30;

switch($x){
    case '20':
    case 20:
        $nop = 0;;;
        if ($y > 29)
            if ($y == 30)
                return 'success';
            else
                return 'fail';
    default:
        return 'fail';
}

return 'fail';
