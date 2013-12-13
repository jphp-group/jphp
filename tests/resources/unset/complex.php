<?php

$x = 'fail';
$y = array('x' => new stdClass());
$y['x']->prop = 'fail';

unset($x, $y['x']->prop);

return 'success' . $x . $y['x']->prop;
