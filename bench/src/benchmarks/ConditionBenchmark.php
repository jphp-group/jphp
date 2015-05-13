<?php
namespace benchmarks;

class ConditionBenchmark extends Benchmark
{
    public function getName()
    {
        return "condition";
    }

    public function run()
    {
        $k = 0;
        $test = 'abc';

        for ($i = 0; $i < self::DEFAULT_ITERATIONS; $i++) {
            if (true) { }
            if (false) { }

            $k++;

            if ($k > 5) { $k = 0; }

            switch ($k) {
                case 1:
                    break;
                case 2:
                case 3:
                   break;
                default:
                    break;
            }

            switch ($test) {
                case 'xyz':
                    break;
                case 'bar':
                case 'foo':
                    break;
                case 'abc':
                    break;
            }

            if ($k < 1) {
            } elseif ($k < 2) {
            } elseif ($k < 3) {
            } elseif ($k < 4) {
            } else {
            }
        }
    }
}