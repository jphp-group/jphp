--TEST--
Reflection Class -> getConstant
--FILE--
<?php
define('DYN', 333);

class Bar {
    const Y1 = "foobar";
}

class Foo extends Bar {
    const X3 = self::X2;
    const X1 = 20;
    const X2 = DYN;
}

$reflectionClass = new ReflectionClass('Foo');
var_dump($reflectionClass->getConstant("X1"));
var_dump($reflectionClass->getConstant("X2"));
var_dump($reflectionClass->getConstant("X3"));
var_dump($reflectionClass->getConstant("Y1"));

var_dump($reflectionClass->getConstants());

--EXPECT--
int(20)
int(333)
int(333)
string(6) "foobar"
array(4) {
  ["Y1"]=>
  string(6) "foobar"
  ["X3"]=>
  int(333)
  ["X1"]=>
  int(20)
  ["X2"]=>
  int(333)
}
