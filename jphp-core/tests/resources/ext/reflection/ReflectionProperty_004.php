--TEST--
Reflection Property -> get set ?
--FILE--
<?php

class Bar {
    public $dynamic;
}

class Foo extends Bar {
    protected static $static = 'foobar';
}

$r = new ReflectionProperty('Foo', 'static');
try {
    var_dump($r->getValue());
} catch (ReflectionException $e){
    var_dump($e->getMessage());
}

try {
    var_dump($r->setValue(null, "foobar"));
} catch (ReflectionException $e){
    var_dump($e->getMessage());
}

$r->setAccessible(true);
var_dump($r->getValue());
$r->setValue(null, "changed");
var_dump($r->getValue());

$r->setAccessible(false);
try {
    var_dump($r->getValue());
} catch (ReflectionException $e){
    var_dump($e->getMessage());
}

--EXPECT--
string(44) "Cannot access non-public member Foo::$static"
string(44) "Cannot access non-public member Foo::$static"
string(6) "foobar"
string(7) "changed"
string(44) "Cannot access non-public member Foo::$static"
