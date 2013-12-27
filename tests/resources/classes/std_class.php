<?php

$obj = new stdClass();
$obj->x = 20;
$obj->y = 30;
$obj->z = 40;

unset($obj->z);
if (isset($obj->z) && !isset($obj->x) && !isset($obj->y)){
    return 'fail_isset';
}

if (!empty($obj->z)){
    return 'fail_empty';
}

if (empty($obj->x) || empty($obj->y))
    return 'fail_empty_xy';

return $obj->x + $obj->y + $obj->z === 50 ? 'success' : 'fail';
