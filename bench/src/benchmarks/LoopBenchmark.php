<?php
namespace benchmarks;

class LoopBenchmark extends Benchmark
{
    public function getName()
    {
        return 'loop';
    }

    public function run()
    {
        for ($i = 0; $i < self::DEFAULT_SMALL_ITERATIONS; $i++)
            for ($j = 0; $j < self::DEFAULT_SMALL_ITERATIONS; $j++)
                for ($k = 0; $k < self::DEFAULT_SMALL_ITERATIONS; $k++)
                    for ($l = 0; $l < self::DEFAULT_SMALL_ITERATIONS; $l++) {}

        $i = 0;
        $j = 0;

        while ($i++ < self::DEFAULT_SMALL_ITERATIONS) {
            do {
            } while ($j++ < self::DEFAULT_SMALL_ITERATIONS);
        }

        $array = ['a' => 1, 'b' => 2, 'c' => 3];

        for ($i = 0; $i < self::DEFAULT_SMALL_ITERATIONS; $i++) {
            foreach ($array as $key => $value) {}
            foreach ($array as &$value) { $value++; }
            foreach ($array as $value) {}
        }
    }
}