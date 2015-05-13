<?php

use benchmarks\ArrayAccessBenchmark;
use benchmarks\ArrayBenchmark;
use benchmarks\Benchmark;
use benchmarks\ClosureBenchmark;
use benchmarks\ConditionBenchmark;
use benchmarks\ConstantCallBenchmark;
use benchmarks\FetchConstantsBenchmark;
use benchmarks\FibonacciBenchmark;
use benchmarks\GetterSetterBenchmark;
use benchmarks\LoopBenchmark;
use benchmarks\NewObjectBenchmark;
use benchmarks\ObjectPropertyBenchmark;
use benchmarks\SimpleFuncCallBenchmark;
use benchmarks\SimpleMethodCallBenchmark;
use benchmarks\SingletonBenchmark;
use benchmarks\StringBenchmark;

include __DIR__ . '/benchmarks/Benchmark.php';

ob_implicit_flush(1);

// only for PHP
if (!class_exists('php\\lang\\Module')) {
    spl_autoload_register(function ($class) {
        include __DIR__ . '/' . str_replace('\\', '/', $class) . '.php';
    });
}

Benchmark::register(new FibonacciBenchmark());
Benchmark::register(new LoopBenchmark());
Benchmark::register(new ConditionBenchmark());
Benchmark::register(new FetchConstantsBenchmark());
Benchmark::register(new ConstantCallBenchmark());
Benchmark::register(new SimpleFuncCallBenchmark());
Benchmark::register(new SimpleMethodCallBenchmark());
Benchmark::register(new NewObjectBenchmark());
Benchmark::register(new ObjectPropertyBenchmark());
Benchmark::register(new ArrayBenchmark());
Benchmark::register(new StringBenchmark());
Benchmark::register(new ClosureBenchmark());
Benchmark::register(new SingletonBenchmark());
Benchmark::register(new ArrayAccessBenchmark());
Benchmark::register(new GetterSetterBenchmark());

$result = Benchmark::start();

echo "\nJPHP Bench Results:\n";

$total = 0;

foreach ($result as $type => $stat) {
    $total += $stat;
    echo "\n -> " . $type . ": " . round($stat * 1000) . " ms";
}

echo "\n\n-------- ";
echo "total: " . round($total, 2) . " s.\n";