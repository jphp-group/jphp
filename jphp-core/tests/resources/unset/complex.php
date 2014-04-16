<?php

$x = 'fail';
$y = array('x' => new stdClass());
$y['x']->prop = 'fail';

unset($x, $y['x']->prop);

$foobar = 'foobar';
unset(${'foobar'});
if ($foobar !== null)
    return 'fail_2: unset by var-var';

return 'success' . $x . $y['x']->prop;
