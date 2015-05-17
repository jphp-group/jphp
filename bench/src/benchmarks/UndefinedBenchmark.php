<?php
namespace benchmarks;


class UndefinedBenchmark extends Benchmark
{
    public function getName()
    {
        return "undefined";
    }

    public function run()
    {
        $o = new \stdClass();

        $arr = [];

        for ($i = 0; $i < self::DEFAULT_SMALL_ITERATIONS; $i++) {
            $const = UNDEF_CONST;
            $prop  = $o->undef;
            $key   = $arr['undef'];
        }
    }
}