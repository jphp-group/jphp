--TEST--
Reflection Class -> getProperties
--FILE--
<?php
class Foo {
    public    $foo  = 1;
    protected $bar  = 2;
    private   $baz  = 3;
    static $foobar = 4;
}

$foo = new Foo();

$reflect = new ReflectionClass($foo);
$props   = $reflect->getProperties(ReflectionProperty::IS_PUBLIC | ReflectionProperty::IS_PROTECTED);

foreach ($props as $prop) {
    print $prop->getName() . "\n";
}

echo "\n\n";

$props   = $reflect->getProperties(ReflectionProperty::IS_STATIC | ReflectionProperty::IS_PRIVATE);
foreach ($props as $prop) {
    print $prop->getName() . "\n";
}

echo "\n\n";

$props   = $reflect->getProperties();
foreach ($props as $prop) {
    print $prop->getName() . "\n";
}
--EXPECT--
foo
bar


foobar
baz


foobar
foo
bar
baz
