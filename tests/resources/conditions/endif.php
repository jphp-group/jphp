<?php

$x = 20;

if ($x == 20):
    $x = 30;
endif;

if ($x == 20):
    $x = 100500;
else:
    $x = 40;
endif;

if ($x == 20):
    return 'fail';
elseif($x == 30):
    return 'fail';
elseif($x == 40):
    return 'success';
else:
    return 'fail';
endif;

return 'fail';
