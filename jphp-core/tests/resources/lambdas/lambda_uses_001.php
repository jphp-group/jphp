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

function test($x) {
    return fn => $x + 1;
};

$lambda = test(2);
if ($lambda() !== 3) {
    return "fail_4: arg of parent";
}


// -----------------------------
$x = 2;
$test = function () use ($x) {
    return fn => $x + 1;
};

$lambda = $test();

if ($lambda() !== 3) {
    return "fail_5: arg of closure parent with use";
}

// ----------------------------

$x = 2;
$test = fn => {
    return fn => $x + 1;
};

$lambda = $test();

if ($lambda() !== 3) {
    return "fail_6: arg of lambda parent with use";
}


$x = 2;
$test = function () {
    return fn => $x + 1;
};

$lambda = $test();

if ($lambda() !== 1) {
    return "fail_7: arg of closure parent without use";
}

return "success";