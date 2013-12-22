<?php

for($i = 0; $i < 10; $i++){
    $func[] = function() use ($i) {
        echo $i . "\n";
    };
}

foreach($func as $el){
    $el();
}