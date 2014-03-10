<?php

$x = 'foo';
$y =& $x;
$z =& $y;

$z = 'foobar';

return $x . '|' . $y . '|' . $z;
