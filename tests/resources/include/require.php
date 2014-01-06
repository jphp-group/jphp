<?php

$result = require __DIR__ . '/inc.simple_include.php';

if ($result !== 'success')
    return 'fail_1';

if ('success' !== require __DIR__ . '/inc.simple_include.php')
    return 'fail_2';

if ('success' !== require(__DIR__ . '/inc.simple_include.php'))
    return 'fail_3';

return 'success';
