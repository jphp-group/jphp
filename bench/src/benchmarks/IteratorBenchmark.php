<?php
namespace benchmarks;

class IteratorBenchmark_Iterator implements \Iterator {
    protected $index;
    protected $values;

    function __construct(array $values)
    {
        $this->values = array_values($values);
    }

    public function current()
    {
        return $this->values[$this->index];
    }

    public function next()
    {
        $this->index++;
    }

    public function key()
    {
        return $this->index;
    }

    public function valid()
    {
        return ($this->index < sizeof($this->values));
    }

    public function rewind()
    {
        $this->index = 0;
    }
}

class IteratorBenchmark extends Benchmark
{
    public function getName()
    {
        return 'iterator';
    }

    public function run()
    {
        $r = 0;
        for ($i = 0; $i < self::DEFAULT_SMALL_ITERATIONS; $i++) {
            $iterator = new IteratorBenchmark_Iterator([1, 2, 3, 4, 5]);

            foreach ($iterator as $value) {
                $r += $value;
            }
        }
    }
}