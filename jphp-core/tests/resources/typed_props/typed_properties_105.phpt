--TEST--
Bug #77673 ReflectionClass::getDefaultProperties returns spooky array
--FILE--
<?php
class A {
    public B $c;
}

$class = new ReflectionClass(A::class);

$defaults = $class->getDefaultProperties();

var_dump($defaults);
var_dump(\php\lib\arr::hasKey($defaults, 'c'));
?>
--EXPECT--
array(0) {
}
bool(false)