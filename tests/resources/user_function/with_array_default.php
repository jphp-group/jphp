<?php

function test($arr = array(1, 2, 3)){
    print_r($arr);
    return $arr[0] + $arr[1] + $arr[2];
}

return test() + test(array(2, 2)); // 10
