<?php

use php\lib\items;
use php\lib\str;
use php\util\Flow;
use php\util\Scanner;


$t = microtime(1);

$arr = [3, 5, $b = ['foobar'], 0, 2];
$sorted = items::sort($arr);
var_dump($sorted);
$sorted[2][0] = 'fail';
var_dump($b);


echo microtime(1) - $t;
