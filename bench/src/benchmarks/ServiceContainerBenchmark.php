<?php
namespace benchmarks;

class ServiceContainerBenchmark_DI
{
    protected static $instances = [];
    protected static $factories = [];

    public static function get($name)
    {
        if ($factory = self::$factories[$name]) {
            return $factory();
        }

        return null;
    }

    public static function getSingleton($class)
    {
        $class = strtolower($class);

        if ($instance = self::$instances[$class]) {
            return $instance;
        }

        $instance = new $class();
        self::$instances[$class] = $instance;

        return $instance;
    }

    public static function setFactory($name, callable $factory)
    {
        self::$factories[$name] = $factory;
    }
}

class ServiceContainerBenchmark extends Benchmark
{
    public function getName()
    {
        return "service container";
    }

    public function run()
    {
        ServiceContainerBenchmark_DI::setFactory('foobar', function () {
            return 'foobar';
        });

        for ($i = 0; $i < self::DEFAULT_SMALL_ITERATIONS * 4; $i++) {
            $singleton = ServiceContainerBenchmark_DI::getSingleton('stdClass');
            $one = ServiceContainerBenchmark_DI::get('foobar');
        }

        $two = ServiceContainerBenchmark_DI::get('unknown');
    }
}