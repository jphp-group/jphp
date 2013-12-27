<?php

$arr = [1, 2, 3, 4];
$sum = 0;
foreach($arr as $el){
    $sum += $el;
}

$index = 0;
foreach($arr as $i => &$el){
    $el = 2;
    $index += $i;
}

foreach($arr as $el){
    $sum += $el; // 2
}

$str = '';
$hash = ['y' => 2, 'x' => 3];
foreach($hash as $key => $el){
    $str .= $key;
    $sum -= $el;
}

foreach($arr as $i => $el){
    unset($arr[$i]);
}

$hash2 = $hash;
foreach($hash2 as &$el){
    $el = 0;
}

$sum2 = $hash['x'] + $hash['y'] + $hash2['x'] + $hash2['y'];

return $sum === 13 && $index === 6 && $str === 'yx'
    && sizeof($arr) === 0 && $sum2 == 5 ? 'success' : 'fail';
