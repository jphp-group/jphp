--TEST--
Reflection Class -> isInstance ?
--FILE--
<?php

interface A { }
class Bar { }
class Foo extends Bar implements A { }

$class = new ReflectionClass('Foo');
$interface = new ReflectionClass('A');
$parent = new ReflectionClass('Bar');
$arg = new Foo;

if ($class->isInstance($arg)) {
    echo "origin: Yes\n";
}

if ($interface->isInstance($arg)) {
    echo "interface: Yes\n";
}

if ($parent->isInstance($arg)) {
    echo "parent: Yes\n";
}

if ($arg instanceof Foo) {
    echo "Yes\n";
}

if (is_a($arg, 'Foo')) {
    echo "Yes\n";
}

--EXPECT--
origin: Yes
interface: Yes
parent: Yes
Yes
Yes
