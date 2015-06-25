<?php
namespace benchmarks;

use php\lib\Str;

class StringBenchmark extends Benchmark
{
    public function getName()
    {
        return "string";
    }

    public function runJphp()
    {
        $str1 = 'foobar';

        $len = Str::length($str1);

        $pos = Str::pos($str1, 'oba');
        $pos = Str::pos($str1, 'oba', 4);

        $upper = Str::upper($str1);

        $replace = Str::replace($str1, 'o', 'O');
    }

    public function runZendPhp()
    {
        $str1 = 'foobar';

        $len = strlen($str1);

        $pos = strpos($str1, 'oba');
        $pos = strpos($str1, 'oba', 4);

        $upper = strtoupper($str1);

        $replace = str_replace('o', 'O', $str1);
    }

    public function run()
    {
        $isJphp = $this->isJphp;

        $str1 = 'foobar';

        $count = self::DEFAULT_ITERATIONS / 4;

        for ($i = 0; $i < $count; $i++) {
            if ($isJphp) {
                $this->runJphp();
            } else {
                $this->runZendPhp();
            }

            $chars = $str1[0] . $str1[2];
            $str1[0] = 'F';

            $sb = '';
            for ($j = 0; $j < 10; $j++) {
                $sb .= $str1;
            }

            for ($j = 0; $j < 4; $j++) {
                $sb = $sb . $chars;
            }

            $isb = "$chars foobar $chars foobar $str1 xyz $str1";
        }
    }
}