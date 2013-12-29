<?php

$i = 0;

if ($i++ !== 0)
    return 'fail_1';

if (++$i !== 2)
    return 'fail_2';

if ($i-- !== 2)
    return 'fail_3';

if (--$i !== 0)
    return 'fail_4';

return 'success';