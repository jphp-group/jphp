<?php
namespace benchmarks;

use ArrayAccess;
use Countable;

class ArrayAccessBenchmark_Object implements ArrayAccess, Countable
{
    public $data = [];

    public function offsetExists($offset)
    {
        return isset($this->data[$offset]);
    }

    public function offsetGet($offset)
    {
        return $this->data[$offset];
    }

    public function offsetSet($offset, $value)
    {
        if (isset($offset)) {
            $this->data[$offset] = $value;
        }
    }

    public function offsetUnset($offset)
    {
        unset($this->data[$offset]);
    }

    public function count()
    {
        return sizeof($this->data);
    }
}

class ArrayAccessBenchmark extends Benchmark
{
    public function getName()
    {
        return "array access object";
    }

    public function run()
    {
        $o = new ArrayAccessBenchmark_Object();

        $get = 0;

        for ($i = 0; $i < self::DEFAULT_SMALL_ITERATIONS; $i++) {
            $o[] = $i;

            $o['x' . $i] = $i;

            $get += $o['x' . $i] + $o[$i] + sizeof($o) + $o[$i];

            if ($i % 3 === 0) {
                $check = isset($o['x' . $i]);
                unset($o[$i]);
            }
        }
    }
}