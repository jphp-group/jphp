<?php
namespace benchmarks;

function test1() {
    return 10;
}

function test2($a, $b, $c) {
    return 20;
}

class ConstantCallBenchmark extends Benchmark
{
    static function test1()
    {
        return 10;
    }

    static function test2($a, $b, $c)
    {
        return 20;
    }

    public function getName()
    {
        return "constant call";
    }

    public function run()
    {
        $r = 0;

        for ($i = 0; $i < self::DEFAULT_ITERATIONS; $i++) {
            $r += test1() + test2(1, 2, 3) + self::test1() + self::test2(1, 2, 3);
        }
    }
}