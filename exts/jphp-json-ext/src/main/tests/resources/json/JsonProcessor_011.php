--TEST--
JsonProcessor test #11: Serialize
--FILE--
<?php

use php\format\JsonProcessor;
use php\lib\items;

class Foo {
    public $x = 10, $y = 20;
}

class Bar extends Foo { public $z = 30; }

$json = new JsonProcessor();

$json->onClassSerialize('Foo', function(Foo $val) {
    return "X:$val->x, Y:$val->y";
});

echo ($json->format(new Foo())), "\n";
echo ($json->format(new Bar())), "\n";

$json->onClassSerialize('Bar', function(Foo $val) {
    return "X:$val->x, Y:$val->y, Z:$val->z";
});
echo ($json->format(new Bar())), "\n";

$json->onClassSerialize('Foo', null);
echo ($json->format(new Foo())), "\n";
echo ($json->format(new Bar())), "\n";

?>
--EXPECTF--
"X:10, Y:20"
"X:10, Y:20"
"X:10, Y:20, Z:30"
{"x":10,"y":20}
"X:10, Y:20, Z:30"
