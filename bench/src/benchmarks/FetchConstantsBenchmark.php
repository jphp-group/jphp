<?php
namespace benchmarks;

const EXTERNAL_STATIC_CONST = 1;
define('EXTERNAL_DYNAMIC_CONST', 2);

error_reporting(0);

class FetchConstantsBenchmark extends Benchmark
{
    const CLASS_CONST = 3;

    public function getName()
    {
        return "fetch constants";
    }

    public function run()
    {
        define('DYNAMIC_CONST', 4);

        $r = 0;

        for ($i = 0; $i < self::DEFAULT_ITERATIONS; $i++) {
            $r += EXTERNAL_STATIC_CONST
                + EXTERNAL_DYNAMIC_CONST
                + self::CLASS_CONST
                + FetchConstantsBenchmark::CLASS_CONST
                + DYNAMIC_CONST;
        }

        for ($i = 0; $i < self::DEFAULT_SMALL_ITERATIONS; $i++) {
            $r = UNDEFINED_CONST;
        }
    }
}