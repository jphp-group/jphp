<?php

function test(&$ref){
    $ref = 'foobar';
}

$var = 100500;
test($var);

return $var;
