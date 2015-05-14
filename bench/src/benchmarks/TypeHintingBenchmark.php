<?php
namespace benchmarks;

use stdClass;

function TypeHintingBenchmark_test1($a, array $b, callable $c, stdClass $d, stdClass $e = null) {}

class TypeHintingBenchmark extends Benchmark
{
    public function getName()
    {
        return "type hinting";
    }

    public function run()
    {
        $arr = [];
        $func = function () {};
        $std = new stdClass();

        for ($i = 0; $i < self::DEFAULT_ITERATIONS; $i++) {
            TypeHintingBenchmark_test1($i, $arr, $func, $std, null);
        }
    }
}