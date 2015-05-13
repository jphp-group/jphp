<?php
namespace benchmarks;

function fibonacci($n)
{
	if ($n < 3) {
		return 1;
	} else {
		return fibonacci($n - 1) + fibonacci($n - 2);
	}
}

class FibonacciBenchmark extends Benchmark
{
	public function getName()
	{
		return "fibonacci";
	}

	public function run()
	{
		$r = 0;

		for ($n = 1; $n <= 5; $n++) {
			$r += fibonacci($n);
		}
	}
}
