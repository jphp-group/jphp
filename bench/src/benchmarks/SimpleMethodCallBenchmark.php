<?php
namespace benchmarks;


class SimpleMethodCallBenchmark extends Benchmark
{
    public static function test1()
    {
        return 1;
    }

    public static function test2($x, $y)
    {
        return 1;
    }

    public static function test3($x, $y, $a = 3)
    {
        return 1;
    }

    protected static function test4($x, $y, $a = 3)
    {
        return 1;
    }

    public function test5()
    {
        return 1;
    }

    public function test6($x, $y)
    {
        return 1;
    }

    protected function test7($x, $y, $a = 3, $b = 3)
    {
        return 1;
    }

    protected function test8($x, $y)
    {
        return 1;
    }

    public function getName()
    {
        return "simple method call";
    }

    public function run()
    {
        $r = 0;

        for ($i = 0; $i < self::DEFAULT_ITERATIONS; $i++) {
            $r += self::test1() + self::test2($i, $i) + self::test3($i, $i, $i) + static::test2($i, $i) + self::test4($i, $i, $i)
                + $this->test5() + $this->test6($i, $i) + $this->test7($i, $i, $i) + $this->test8($i, $i)
                + $this->test2($i, $i);
        }
    }
}