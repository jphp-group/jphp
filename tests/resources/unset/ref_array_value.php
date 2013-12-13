<?php

$arr = array('x' => 'success');
$arr2['x'] =& $arr['x'];
unset($arr2['x']);

return $arr['x'] . $arr2['x'];
