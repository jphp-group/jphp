<?php

function func1() {
    function func2() {
        return 'success2';
    }
    $foobar = 'success';
    return $foobar;
}

if (func1() !== 'success')
    return 'fail_1';

if (func2() !== 'success2')
    return 'fail_2';

return 'success';
