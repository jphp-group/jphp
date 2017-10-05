--TEST--
Reflection Class -> newInstance
--FILE--
<?php

class Foo {
    var $x = 10, $y = 20;

    function __construct($x, $y){
        echo __CLASS__ . " construct trigger ($x, $y).\n";
    }
}

class Bar extends Foo {
    var $z = 40;

    function __construct($a){
        parent::__construct($a, 'test');
        echo __CLASS__ . " construct trigger.\n";
    }
}

$r1 = new ReflectionClass('Foo');
$o1 = $r1->newInstance(100, 500);
var_dump($o1);

$r2 = new ReflectionClass('Bar');
$o2 = $r2->newInstance('foobar');
var_dump($o2);

echo "\nCheck newInstanceWithoutConstructor:\n";
$o3 = $r2->newInstanceWithoutConstructor();
var_dump($o3);

echo "\n";

$r2 = new ReflectionClass('Bar');
$o2 = $r2->newInstanceArgs(['foobar2']);
var_dump($o2);

$r2 = new ReflectionClass('Bar');
$o2 = $r2->newInstanceArgs('foobar2');
var_dump($o2);

?>
--EXPECTF--
Foo construct trigger (100, 500).
object(Foo)#%d (2) {
  ["x"]=>
  int(10)
  ["y"]=>
  int(20)
}
Foo construct trigger (foobar, test).
Bar construct trigger.
object(Bar)#%d (3) {
  ["z"]=>
  int(40)
  ["x"]=>
  int(10)
  ["y"]=>
  int(20)
}

Check newInstanceWithoutConstructor:
object(Bar)#%d (3) {
  ["z"]=>
  int(40)
  ["x"]=>
  int(10)
  ["y"]=>
  int(20)
}

Foo construct trigger (foobar2, test).
Bar construct trigger.
object(Bar)#%d (3) {
  ["z"]=>
  int(40)
  ["x"]=>
  int(10)
  ["y"]=>
  int(20)
}

Fatal error: Uncaught TypeError: Argument 1 passed to ReflectionClass::newInstanceArgs() must be of the type array, string given, called in %s on line 39, position %d and defined in Unknown on line 0, position 0
Stack Trace:
#0 {main}
  thrown in Unknown on line 0