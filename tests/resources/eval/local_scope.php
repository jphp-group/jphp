<?php

function test($x){
    return eval('return $x;');
}

if (test('foobar') !== 'foobar')
    return 'fail_1';

return 'success';