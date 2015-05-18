<?php

use benchmarks\ArrayAccessBenchmark;
use benchmarks\ArrayBenchmark;
use benchmarks\Benchmark;
use benchmarks\ClosureBenchmark;
use benchmarks\ConditionBenchmark;
use benchmarks\ConstantCallBenchmark;
use benchmarks\DynamicAccessBenchmark;
use benchmarks\FetchConstantsBenchmark;
use benchmarks\FibonacciBenchmark;
use benchmarks\GetterSetterBenchmark;
use benchmarks\IteratorBenchmark;
use benchmarks\LoopBenchmark;
use benchmarks\MathBenchmark;
use benchmarks\NewObjectBenchmark;
use benchmarks\ObjectPropertyBenchmark;
use benchmarks\ServiceContainerBenchmark;
use benchmarks\SimpleFuncCallBenchmark;
use benchmarks\SimpleMethodCallBenchmark;
use benchmarks\SingletonBenchmark;
use benchmarks\StringBenchmark;
use benchmarks\TypeHintingBenchmark;
use benchmarks\UndefinedBenchmark;

include __DIR__ . '/benchmarks/Benchmark.php';

ob_implicit_flush(1);

// only for PHP
if (!class_exists('php\\lang\\Module')) {
    spl_autoload_register(function ($class) {
        include __DIR__ . '/' . str_replace('\\', '/', $class) . '.php';
    });
}

$result = Benchmark::start();
Benchmark::showResult($result);