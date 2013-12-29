<?php

$result = @eval('return '); // parse error

if ($result !== false)
    return 'fail_parse';

return 'success';