<?php

$e -= ($b1[6]*$b2[6])/sqrt($dx*$dx + $dy*$dy + $dz*$dz);

$y = 222;
$b = true;
$a = $b === true && ($c = $y == 222 ? 'yes' : 'no');

if ($a !== true)
    return "fail_1: \$a == $a, but must true";

if ($c !== 'yes')
    return 'fail_2';

if (!!true !== !$unknown)
    return 'fail_3';

return $e == 0 ? 'success' : 'fail';
