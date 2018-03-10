<?php

$a = 10;
$b = 'string';

$lambda = fn => {
    return $a;
};

if ($lambda() !== 10) {
    return "fail_1";
}

$lambda = fn($a) => $a;

if ($lambda(5) !== 5) {
    return "fail_2";
}

$lambda = fn() => $a . $b;

if ($lambda() !== "10string") {
    return "fail_3";
}

return "success";