<?php

$x = 'foobar';
$obj = (object)$x;

if ($obj->scalar !== 'foobar' || !($obj instanceof stdClass))
    return 'fail_scalar';

$x = ['x' => 20, 'y' => 30];
$obj = (object)$x;

if ($obj->x !== 20 || $obj->y !== 30 || !($obj instanceof stdClass))
    return 'fail_array';

class Foobar {}
$x = new Foobar;
$obj = (object)$x;

if (!($obj instanceof Foobar))
    return 'fail_object';


return 'success';