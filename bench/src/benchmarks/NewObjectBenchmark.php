<?php
namespace benchmarks;

use stdClass;

class NewObjectBenchmark_A {
    public $x, $y;
    public $array = [1, 2, 3, 4, 'x' => 5];
    protected $z = 123;
}

class NewObjectBenchmark_B extends NewObjectBenchmark_A {
    private $a, $b;
}

class NewObjectBenchmark_C extends NewObjectBenchmark_B {
    private $a = 20;
}

class NewObjectBenchmark_D extends NewObjectBenchmark_C {
    public function __construct() {
    }
}

class NewObjectBenchmark extends Benchmark
{
    public function getName()
    {
        return "new object";
    }

    public function run()
    {
        for ($i = 0; $i < self::DEFAULT_ITERATIONS; $i++) {
            $o1 = new stdClass();

            $o2 = new NewObjectBenchmark_A();
            $o3 = new NewObjectBenchmark_B();
            $o4 = new NewObjectBenchmark_C();
            $o5 = new NewObjectBenchmark_D();
        }
    }
}