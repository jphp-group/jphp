<?php

$x = 1;
$y = 1.5;

if ((string )$x !== '1')
    return 'fail_string';

if ((double)$x !== 1.0 || ( float )$x !== 1.0 || (real)$x !== 1.0 )
    return 'fail_double';

if ((boolean)$x !== true || (Bool)$x !== true)
    return 'fail_bool';

if ((unset)$x !== NULL)
    return 'fail_unset';

if ((int)$y !== 1 || (integer)$y !== 1)
    return 'fail_int';


return 'success';