--TEST--
Reflection Class -> getInterfaces
--FILE--
<?php

interface C { }
interface A extends C { }
interface B { }

class Bar implements C {

}

class Foo extends Bar implements A, B {

}

$r = new ReflectionClass('ReflectionParameter');
var_dump($r->getInterfaceNames());

$r = new ReflectionClass('Foo');
var_dump($r->getInterfaceNames());

$r = new ReflectionClass('Bar');
var_dump($r->getInterfaceNames());

--EXPECTF--
array(1) {
  [0]=>
  string(9) "Reflector"
}
array(3) {
  [0]=>
  string(1) "C"
  [1]=>
  string(1) "A"
  [2]=>
  string(1) "B"
}
array(1) {
  [0]=>
  string(1) "C"
}
