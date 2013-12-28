<?php


for($i = 0; $i < 1000; $i++){
    $x = eval('$result = 20 + $i * 40 + cos(11);');
}

var_dump($x);