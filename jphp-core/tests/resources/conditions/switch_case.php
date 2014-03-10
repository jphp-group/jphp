<?php

$x = 100500;

switch (true){
    case false:
    case $x == 100500: return 'success';
    default:
        return 'fail_default';
}

return 'fail';
