<?php

for($i = 0; $i < 5; $i++){
    $func[] = function() use ($i){
        return $i;
    };
}

$sum = 0;
foreach($func as $el){
    $sum += $el();
}

if ($sum !== 10)
    return "fail_1: $sum != 10";

return 'success';
