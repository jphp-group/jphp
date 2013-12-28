<?php

$result = @eval('return '); // parse error

if ($result !== false)
    return 'fail_parse';


$result = @eval('$x->prop = 30;'); // invalid fatal error

if ($result !== false)
    return 'fail_fatal';

return 'success';