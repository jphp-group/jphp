<?php

$external = 2;
$func1 = function($i){
    return $i + 1;
};

$func2 = function($i) use ($external){
    return $i + $external;
};

if ($func1(2,) !== 3)
    return 'fail_1';

if ($func2(2) !== 4)
    return 'fail_2';


return 'success';
