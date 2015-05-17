<?php
namespace benchmarks;

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

	static function start() {
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
}
