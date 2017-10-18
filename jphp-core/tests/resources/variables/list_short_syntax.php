<?php

[$x, $y] = [10, 20];

if ($x + $y !== 30)
    return 'fail_1: simple';

[, $x,, $y] = [10, 20, 30, 40];

if ($x + $y !== 60)
    return 'fail_3: with skip';

[$x, [$a, $b], $y] = [10, [20, 30], 40];

if ($x + $a + $b + $y !== 100)
    return 'fail_4: nested';

if (([$x, $y] = [10, 20]) != [10, 20])
    return 'fail_5: returned';

[$x, $y] = ['x'=>10, 20];

if ($x !== 20 || $y !== NULL)
    return 'fail_6: hashed';

[$a, $a] = [10, 20];
if ($a !== 20)
    return 'fail_7: duplicate';

[$b[], $b[]] = [10, 20];
if ($b[0] !== 10 && $b[1] !== 20)
    return 'fail_8: array push';


$arr = [[1, 2], [3, 4]];
$result = 0;
foreach ($arr as [$x, $y]) {
    $result += $x + $y;
}
if ($result !== 10) {
    return 'fail_9: foreach + list';
}

return 'success';
