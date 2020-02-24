<?php

$obj = new stdClass();
$obj->x = 'success';
$obj->y = 'fail';
$obj->z = 'fail';

unset($obj->y, $obj->z,);

return $obj->x . $obj->y . $obj->z;
