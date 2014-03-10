<?php

function &test(&$item){
    return $item;
}

$arr['x'] = 10;
$arr['y'] = 20;

$a =& test($arr['x']);
$b =  test($arr['y']);

$a = 20;
$b = 30;

return $arr['x'] + $arr['y']; // 40
