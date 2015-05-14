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

Benchmark::register(new FibonacciBenchmark());
Benchmark::register(new LoopBenchmark());
Benchmark::register(new ConditionBenchmark());
Benchmark::register(new MathBenchmark());
Benchmark::register(new FetchConstantsBenchmark());
Benchmark::register(new ConstantCallBenchmark());
Benchmark::register(new SimpleFuncCallBenchmark());
Benchmark::register(new SimpleMethodCallBenchmark());
Benchmark::register(new TypeHintingBenchmark());
Benchmark::register(new NewObjectBenchmark());
Benchmark::register(new ObjectPropertyBenchmark());
Benchmark::register(new ArrayBenchmark());
Benchmark::register(new StringBenchmark());
Benchmark::register(new ClosureBenchmark());
Benchmark::register(new UndefinedBenchmark());
Benchmark::register(new SingletonBenchmark());
Benchmark::register(new ArrayAccessBenchmark());
Benchmark::register(new GetterSetterBenchmark());
Benchmark::register(new IteratorBenchmark());
Benchmark::register(new ServiceContainerBenchmark());

$result = Benchmark::start();

echo "\nJPHP Bench Results:\n";

$total = 0;

foreach ($result as $type => $stat) {
    $total += $stat;
    echo "\n -> " . $type . ": " . round($stat * 1000) . " ms";
}

echo "\n\n-------- ";
echo "total: " . round($total, 2) . " s.\n";