<?php
namespace my;

function cos($x){
    return 'success';
}

if (cos(2) !== 'success')
    return 'fail_1';

if (\my\cos(2) !== 'success')
    return 'fail_2';

if (!is_double(sin(0)))
    return 'fail_3';

if (!is_double(\sin(0)))
    return 'fail_4';

return 'success';