<?php
namespace benchmarks;

class GetterSetterBenchmark_Object {
    protected $data = [];

    public function __set($name, $value) {
        $method = "set{$name}";

        if (method_exists($this, $method)) {
            $this->{$method}($value);
        } else {
            $this->data[$name] = $value;
        }
    }

    public function __get($name) {
        $method = "get{$name}";

        if (method_exists($this, $method)) {
            return $this->{$method}();
        }

        return $this->data[$name];
    }

    public function getX() {
        return 1;
    }

    public function getY() {
        return 2;
    }

    public function setY($value) {
    }
}

class GetterSetterBenchmark extends Benchmark
{
    public function getName()
    {
        return "getter + setter";
    }

    public function run()
    {
        $o = new GetterSetterBenchmark_Object();

        $o->first = 123;

        for ($i = 0; $i < self::DEFAULT_SMALL_ITERATIONS; $i++) {
            $o->foo = $i + $o->first;
            $o->foo = $o->foo + $o->first + $o->foo + $o->x + $o->y + $o->x + $o->y;
            $o->y = 123;
            $o->y = 321;
        }
    }
}