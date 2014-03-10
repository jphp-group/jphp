<?php

$x = 20;
$y = 30;
$m = $z;

$arr = compact('x', 'y', 'z');

if ($arr['x'] !== 20)
    return 'fail_x';

if ($arr['y'] !== 30)
    return 'fail_y';

if (isset($arr['z']))
    return 'fail_z';

function test($y){
    $a = 20;
    $b = $z;
    return compact('a', 'y', 'z');
}

$arr = test(30);
if ($arr['a'] !== 20)
    return 'fail_test_a';

if ($arr['y'] !== 30)
    return 'fail_test_y';

if (isset($arr['z']))
    return 'fail_test_z';

return 'success';