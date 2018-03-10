<?php

class Test {

    function make(): Closure
    {
        return fn() => $this;
    }

    function make2(): Closure
    {
        return fn() => fn() => $this;
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

return "success";