<?php
namespace benchmarks;

class MathBenchmark extends Benchmark
{
    const PI = 3.14;

    public function getName()
    {
        return "math";
    }

    public function run()
    {
        $r = 0;
        $const = 3.4;

        for ($i = 0; $i < self::DEFAULT_ITERATIONS; $i++) {
            $r += (($i * 2) / 1.2) ** $i;
            $r *= (($const / 2) - $i) % 2;
            $r = floor($r) + ceil($r) + round($r) - abs($r) + pi() + self::PI;
        }

        // constant functions.
        for ($i = 0; $i < self::DEFAULT_SMALL_ITERATIONS; $i++) {
            $r += abs(ceil(-3.4));
        }
    }
}