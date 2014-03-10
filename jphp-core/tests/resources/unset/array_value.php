<?php

$arr = array('x' => 'success', 'y' => 'fail', 'z' => 'fail');
unset($arr['y'], $arr['z']);

return $arr['x'] . $arr['y'] . $arr['z'];
