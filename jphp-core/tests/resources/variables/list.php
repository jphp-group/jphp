<?php

list($x, $y) = [10, 20];

if ($x + $y !== 30)
    return 'fail_1: simple';

list(, $x,, $y) = [10, 20, 30, 40];

if ($x + $y !== 60)
    return 'fail_3: with skip';

list($x, list($a, $b), $y) = [10, [20, 30], 40];

if ($x + $a + $b + $y !== 100)
    return 'fail_4: nested';

if ((list($x, $y) = [10, 20]) != [10, 20])
    return 'fail_5: returned';

list($x, $y) = ['x'=>10, 20];

if ($x !== 20 || $y !== NULL)
    return 'fail_6: hashed';

list($a, $a) = [10, 20];
if ($a !== 10)
    return 'fail_7: duplicate';

list($b[], $b[]) = [10, 20];
if ($b[0] !== 20 && $b[1] !== 10)
    return 'fail_8: array push';

return 'success';
