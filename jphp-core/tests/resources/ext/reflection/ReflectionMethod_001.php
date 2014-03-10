--TEST--
Reflection Method -> basic
--FILE--
<?php

class Foo {
    private function test(){}
}

var_dump(new ReflectionMethod('Foo', 'test'));
var_dump(new ReflectionMethod(new Foo, 'test'));

try {
    var_dump(new ReflectionMethod('Foo', 'unknown'));
} catch (ReflectionException $e){
    var_dump($e->getMessage());
}

try {
    var_dump(new ReflectionMethod('Unknown', 'unknown'));
} catch (ReflectionException $e){
    var_dump($e->getMessage());
}

--EXPECTF--
object(ReflectionMethod)#%d (2) {
  ["name"]=>
  string(4) "test"
  ["class"]=>
  string(3) "Foo"
}
object(ReflectionMethod)#%d (2) {
  ["name"]=>
  string(4) "test"
  ["class"]=>
  string(3) "Foo"
}
string(31) "Method Foo::unknown() not found"
string(25) "Class 'Unknown' not found"