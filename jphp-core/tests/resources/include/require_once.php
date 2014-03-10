<?php

$i = -1;

require_once __DIR__ . '/inc.once.php';
$x = require_once __DIR__ . '/inc.once.php';

if ($i !== 0)
    return "fail_1: $i != 0";

if ($x !== TRUE)
    return "fail_2";

return 'success';
