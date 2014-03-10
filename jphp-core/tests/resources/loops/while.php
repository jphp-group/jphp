<?php

$i = 0;
$x = 100;
$y = 0;

while($i < 200){
    $i += 1;
    if ($i % 2 == 0)
        $y += 2;

    if ($i % 2 == 0)
        continue;

    $x += 1;
}

while (false);

if ($x === 200 && $y === 200)
    return 'success';
else
    return 'fail';
