<?php

$z = 30;
$x = ($y = ($z += 30)) + 30;

return $x === 90 && $y === 60 && $z === 60 ? 'success' : 'fail';
