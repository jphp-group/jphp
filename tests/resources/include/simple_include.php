<?php

$result = include __DIR__ . '/inc.simple_include.php';

if ($result !== 'success')
    return 'fail';

return 'success';