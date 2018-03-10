<?php

$lambda = fn() => 1;

if ($lambda() !== 1) {
    return "fail_1";
}

$lambda = fn => 2;
if ($lambda() !== 2) {
    return "fail_2: without args and braces";
}

$lambda = fn($x) => $x + 1;
if ($lambda(2) !== 3) {
    return "fail_3: with arg";
}

$lambda = fn($x) => { return $x * $x; };

if ($lambda(3) !== 9) {
    return "fail_4: with arg and return";
}

$lambda = fn => {
    return 1 + 2;
};

if ($lambda() !== 3) {
    return "fail_5: with block";
}

return "success";