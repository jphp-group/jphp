<?php
namespace benchmarks;

class ObjectPropertyBenchmark_A
{
    public $x = 10, $y = 20;
    public $veryLongObjectPropertyWithAnyName = 'foobar';

    protected $a = 10;
    private $b = 20;

    public function setZ($value) {
        $this->a = $value;
        $this->b = $value;
    }
}

class ObjectPropertyBenchmark extends Benchmark
{
    public function getName()
    {
        return "object property";
    }

    public function run()
    {
        $o = new ObjectPropertyBenchmark_A();

        $r = 0;

        $prop = 'y';

        for ($i = 0; $i < self::DEFAULT_ITERATIONS; $i++) {
            $o->setZ($i);

            $r += $o->x + $o->y + $o->x + $o->y + $o->{'x'} + $o->{$prop};
            $str = $o->veryLongObjectPropertyWithAnyName;

            $o->x = $i;

            $check = isset($o->x) && isset($o->undef);
        }

        for ($i = 0; $i < self::DEFAULT_SMALL_ITERATIONS; $i++) {
            $o->undef = $i;
            $check = isset($o->undef);
            unset($o->undef);
        }
    }
}