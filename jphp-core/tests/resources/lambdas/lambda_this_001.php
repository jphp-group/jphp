<?php
$GLOBALS['x'] = 20;

class Test {
    function make(): Closure
    {
        return fn() => $this;
    }

    function make2(): Closure
    {
        return fn() => fn() => $this;
    }

    function make3(): Closure
    {
        return static fn() => "foobar";
    }

    function make4(): Closure
    {
        return fn&() => $GLOBALS['x'];
    }
}

$test = new Test();
$lambda = $test->make();

if ($lambda() !== $test) {
    return "fail_1";
}

$lambda = $test->make2();
$lambda = $lambda();

if ($lambda() !== $test) {
    return "fail_2";
}

$lambda = $test->make3();
if ($lambda() !== "foobar") {
    return "fail_3";
}

$lambda = $test->make4();
$x = $lambda();
$x = 40;
if ($GLOBALS['x'] !== 40) {
    return "fail_4";
}


return "success";