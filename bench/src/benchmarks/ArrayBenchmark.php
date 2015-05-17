<?php
namespace benchmarks;

class ArrayBenchmark extends Benchmark
{
    public function getName()
    {
        return "array";
    }

    public function run()
    {
        $aa1 = [1, 2, 3];
        $aa2 = ['x' => 10, 'y' => 20];

        $x = 'x'; $y = 'y';

        $i0 = 0;
        $i1 = 1;
        $i2 = 2;

        for ($i = 0; $i < self::DEFAULT_ITERATIONS; $i++) {
            $a1 = [1, 2, 3];
            $a2 = ['x' => 1, 'y' => 2];

            $x = $aa2['x'] + $aa2[$x];
            $y = $aa2['y'] + $aa2[$y];

            $a = $aa1[0] + $aa1[1] + $aa1[2] + $aa1[$i0] + $aa1[$i1] + $aa1[$i2];

            $aa1[0] += $i;
            $aa1[$i0] += $i;
            $aa2['x'] += $i;
            $aa2[$x] += $i;
        }
    }
}