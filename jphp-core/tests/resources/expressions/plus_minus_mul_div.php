<?php

$x = 20;
$z = (20 - 40 / $x) * 3 + 10;

$y = 2 ** 6;
$y **= 2;
$z += $y;

return $z === 4160 ? 'success' : 'fail';
