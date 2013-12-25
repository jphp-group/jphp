<?php

function test(){

    for($i=0; $i < 10000000; $i++){
        $func = is_callable(function($x){  });
    }
}

test();