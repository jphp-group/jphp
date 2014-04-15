<?php

$arr['-2'] = 20;
$arr[] = 30;

if ($arr[-2] !== 20)
    return "fail_1";

if ($arr[0] !== 30)
    return "fail_3";

$arr['x'] = 40;
$arr[] = 50;

if ($arr[1] !== 50)
    return "fail_4";

unset($arr[1]);

$arr[] = 60;
if ($arr[2] !== 60)
    return "fail_5";

$arr = [ // @bug issue/92
    true || !!$x,
];
if ($arr[0] !== true)
    return "fail_6";

return "success";
