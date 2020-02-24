<?php

$x = 'foobar';
$y = [1, 2];

$arr = (array)$x;
if ($arr[0] !== 'foobar')
    return 'fail_scalar';

$arr = (array)$y;
if ($y[1] !== 2)
    return 'fail_array';

$z = new stdClass();
$z->x = 1;
$z->y = 2;

$arr = (array)$z;

if ($arr['x'] !== 1 || $arr['y'] !== 2)
    return 'fail_object';


class Xoo {
    protected $x = 20;
    private $y = 30;
    public $z = 40;
}
$foo = new Xoo();
$arr = (array)$foo;

if ($arr["\x0*\x0x"] !== 20)
    return 'fail_object_protected';

if ($arr["\x0Xoo\x0y"] !== 30)
    return 'fail_object_private';

if ($arr['z'] !== 40)
    return 'fail_object_public';


return 'success';