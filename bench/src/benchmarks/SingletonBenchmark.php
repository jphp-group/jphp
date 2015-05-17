<?php
namespace benchmarks;

class SingletonBenchmark_Singleton
{
    private function __construct()
    {
    }

    static function getInstance()
    {
        static $instance;

        if (!$instance) {
            $instance = new static();
        }

        return $instance;
    }
}

class SingletonBenchmark extends Benchmark
{
    public function getName()
    {
        return "singleton pattern";
    }

    public function run()
    {
        $count = self::DEFAULT_ITERATIONS;

        for ($i = 0; $i < $count; $i++) {
            $o = SingletonBenchmark_Singleton::getInstance();
        }
    }
}