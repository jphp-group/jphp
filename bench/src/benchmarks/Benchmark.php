<?php
namespace benchmarks;

use php\http\HttpServer;
use php\lang\System;
//use php\webserver\WebRequest;
//use php\webserver\WebServer;

abstract class Benchmark {
	const DEFAULT_ITERATIONS = 100;
	const DEFAULT_SMALL_ITERATIONS = 8;

	/** @var Benchmark[] */
	protected static $benchmarks = array();

	protected $isJphp;

	function __construct()
	{
		$this->isJphp = class_exists('php\\lang\\Module', false);
	}

	abstract public function getName();
	abstract public function run();

	static function register(Benchmark $benchmark) {
		self::$benchmarks[] = $benchmark;
	}

	static function startServer() {
        self::registerAll();

		$server = new HttpServer(9999);
		$server->get('/', function () {
            self::showResult(self::startBench());
        });

		echo "Bench Server has been started at http://localhost:9999/", "\n", " -> result will be in console output.";
		$server->run();
	}

	static function registerAll() {
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
		Benchmark::register(new NBodyBenchmark());
	}

	static function startBench() {
		$result = array();

		for ($i = 0; $i < self::DEFAULT_ITERATIONS * 100; $i++) {
			foreach (self::$benchmarks as $benchmark) {
				$t = microtime(1);
				$benchmark->run();
				$stat = microtime(1) - $t;

				$result[$benchmark->getName()] += $stat;
			}
		}

		return $result;
	}

	static function start() {
		self::registerAll();

		if (class_exists('php\\lang\\Module', false) && System::getProperty('jphp.benchServer')) {
			self::startServer();
			return [];
		}

		return self::startBench();
	}

	static function showResult($result) {
		echo "\nJPHP Bench Results:\n";

		$total = 0;

		foreach ($result as $type => $stat) {
			$total += $stat;
			echo "\n -> " . $type . ": " . round($stat * 1000) . " ms";
		}

		echo "\n\n-------- ";
		echo "total: " . round($total, 2) . " s.\n";
	}
}
