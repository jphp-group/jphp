<?php

$arr = array('x'=>20, 'y'=>30);
$var =& $arr['z'];
$var = 20;

$var =& $null[1];
$var = 30;

return $arr['z'] + $arr['x'] + $arr['y'] + $null[1];
