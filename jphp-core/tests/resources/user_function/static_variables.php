<?php

function test(){
    static $i = 30;
    $i += 1;
    return $i;
}

test();
test();

return test();

