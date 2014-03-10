<?php

$x = 20;
$y = 30;

$z = eval('$result = $x + $y;');

if ($result !== 50)
    return 'fail_1: result var';

if ($z !== NULL)
    return 'fail_2: eval return null';

return 'success';