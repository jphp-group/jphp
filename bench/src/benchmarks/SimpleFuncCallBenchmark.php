<?php
namespace benchmarks;

function SimpleFuncCallBenchmark_test1() {
    return 1;
}

function SimpleFuncCallBenchmark_test1_empty() {
}

function SimpleFuncCallBenchmark_test2($i) {
    return $i + 1;
}

function SimpleFuncCallBenchmark_test2_empty($i) {
}

function SimpleFuncCallBenchmark_test3($i = 1) {
    return $i;
}

function SimpleFuncCallBenchmark_test3_empty($i = 1) {
    return $i;
}

function SimpleFuncCallBenchmark_test4($i, $j = 1) {
    return $i + $j;
}

function SimpleFuncCallBenchmark_test4_empty($i, $j = 1) {
}

function SimpleFuncCallBenchmark_test5($a, $b, $c, $x = 1, $y = 2, $z = 3) {
    return $a + $b + $c + $x + $y + $z;
}

function SimpleFuncCallBenchmark_test5_empty($a, $b, $c, $x = 1, $y = 2, $z = 3) {
}


class SimpleFuncCallBenchmark extends Benchmark
{
    public function getName()
    {
        return "simple func call";
    }

    public function run()
    {
        $r = 0;

        for ($i = 0; $i < self::DEFAULT_ITERATIONS; $i++) {
            $r += SimpleFuncCallBenchmark_test1()
                + SimpleFuncCallBenchmark_test2($i)
                + SimpleFuncCallBenchmark_test3()
                + SimpleFuncCallBenchmark_test3($i)
                + SimpleFuncCallBenchmark_test4($i)
                + SimpleFuncCallBenchmark_test4($i, $i)
                + SimpleFuncCallBenchmark_test5($i, $i, $i);

            SimpleFuncCallBenchmark_test1_empty();
            SimpleFuncCallBenchmark_test2_empty($i);
            SimpleFuncCallBenchmark_test3_empty();
            SimpleFuncCallBenchmark_test3_empty($i);
            SimpleFuncCallBenchmark_test4_empty($i);
            SimpleFuncCallBenchmark_test4_empty($i, $i);
            SimpleFuncCallBenchmark_test5_empty($i, $i, $i);
        }
    }
}