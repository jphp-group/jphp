--TEST--
Reflection Method -> getClosure
--FILE--
<?php

class Foobar {
    var $x = 100;

    function test($y){ echo "$this->x:$y\n"; return 'SUCCESS'; }
    static function test2($y){ return $y + 1; }
}

$r = new ReflectionMethod('Foobar', 'test');
$foo = new Foobar; $foo->x = 2000;

$func = $r->getClosure($foo);
var_dump($func(3000));

$func = new ReflectionMethod('Foobar', 'test2')->getClosure(null);
var_dump($func(100));
?>
--EXPECT--
2000:3000
string(7) "SUCCESS"
int(101)