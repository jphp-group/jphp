<?php

$powersOfTwo = [1 => 2, 2 => 4, 3 => 8];

list(1 => $oneBit, 2 => $twoBit, 3 => $threeBit) = $powersOfTwo;

if ($oneBit !== 2) return "fail_1";
if ($twoBit !== 4) return "fail_2";
if ($threeBit !== 8) return "fail_3";

/// ---------------------------------------------
$points = [
    ["x" => 1, "y" => 2],
    ["x" => 2, "y" => 1]
];

list(list("x" => $x1, "y" => $y1), list("x" => $x2, "y" => $y2)) = $points;

if ($x1 !== 1 && $y1 !== 2) return "fail_4";
if ($x2 !== 2 && $y2 !== 1) return "fail_5";

/// ---------------------------------------------

$arr = [1, ['x' => 33, 'y' => 22], 99, ['b' => 11, 'a' => 12]];
list(, list('x' => $x, 'y' => $y), $z, list('a' => $a, 'b' => $b),) = $arr;

if ($z !== 99) return 'fail_6';
if ($x !== 33 && $y !== 22) return 'fail_7';
if ($a !== 12 && $b !== 11) return 'fail_8';

/// ----------------------------------------------

$arr = [10, ['x' => 330, 'y' => 220], 990, ['b' => 110, 'a' => 120]];
[, ['x' => $x, 'y' => $y], $z, ['a' => $a, 'b' => $b],] = $arr;

if ($z !== 990) return 'fail_6';
if ($x !== 330 && $y !== 220) return 'fail_7';
if ($a !== 120 && $b !== 110) return 'fail_8';

return 'success';
