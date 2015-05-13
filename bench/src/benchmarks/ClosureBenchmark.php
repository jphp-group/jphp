<?php

namespace benchmarks;

class ClosureBenchmark extends Benchmark
{
    public function getName()
    {
        return "closure";
    }

    public function run()
    {
        $r = 0;

        for ($i = 0; $i < self::DEFAULT_ITERATIONS; $i++) {
            $func = function ($x) { return $x + 1; };

            if ($i % 3 == 0) {
                $func2 = function ($x) use ($i) {
                    return $x + $i;
                };

                $r += $func2($i, $i);
            }

            $r += $func($i);
        }
    }
}