<?php

$arr['x'][0] = 20;
$copy = $arr;
$copy['x'][0] = 40;

return $arr['x'][0] + $copy['x'][0] === 60 ? 'success' : 'fail';