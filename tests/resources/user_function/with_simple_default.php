<?php

function test($x = 100, $y = 2.3, $z = 'foobar', $b = true, $m = null){
    var_dump($x);
    var_dump($y);
    var_dump($z);
    var_dump($b);
    var_dump($m);
}

test();
