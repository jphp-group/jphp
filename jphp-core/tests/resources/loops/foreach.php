<?php

$arr = [1, 2, 3, 4];
$sum = 0;
foreach($arr as $el){
    $sum += $el;
}

if ($sum !== 10)
    return 'fail_1';

$index = 0;
foreach($arr as $i => &$el){
    $el = 2;
    $index += $i;
}

if ($index !== 6)
    return 'fail_2';

foreach($arr as $el){
    $sum += $el; // 2
}

if ($sum !== 18)
    return "fail_3: $sum != 18";

$str = '';
$hash = ['y' => 2, 'x' => 3];
foreach($hash as $key => $el){
    $str .= $key;
    $sum -= $el;
}

if ($str !== 'yx')
    return "fail_4: $str != yx";

if ($sum !== 13)
    return "fail_4: $sum != 13";

foreach($arr as $i => $el){
    unset($arr[$i]);
}

if (sizeof($arr) !== 0)
    return "fail_5: sizeof";

$hash2 = $hash;
foreach($hash2 as &$el){
    $el = 0;
}

$sum2 = $hash['x'] + $hash['y'] + $hash2['x'] + $hash2['y'];
if ($sum2 !== 5)
    return "fail_6: $sum2 != 5";

$sum = 0;
foreach($hash as $el[0][1]){
    $sum += $el[0][1];
}

if ($sum !== 5)
    return "fail_7: $sum != 5";


$sum = 0;
$el = new stdClass();
foreach($hash as $el->prop){
    $sum += $el->prop;
}

if ($sum !== 5)
    return "fail_8: $sum != 5";

foreach(array() as $x);

//////
$arr = [1];
foreach ($arr as $i) {
    echo $i, "\n";
    $arr[] = ++$i;
    if (count($arr) > 2)
        return "fail_9: infinity loop";
}

/////
$sum = 0;
$arr = [1, 2];
foreach ($arr as $i) {
    echo $i, "\n";
    unset($arr[1]);
    $sum += $i;
}

if ($sum !== 1)
    return "fail_10: unset with list array";

return 'success';
