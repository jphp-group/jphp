--TEST--
Reflection Property -> isDefault ?
--FILE--
<?php

define('BAR', 2000);

class Foo {
    var $x = BAR;
    var $y = NULL;
    var $z;
}

$r = new ReflectionProperty(new foo, 'x');
echo "Foo::\$x isDefault: " . ($r->isDefault() ? 'true' : 'false') . "\n";

$r = new ReflectionProperty(new foo, 'y');
echo "Foo::\$y isDefault: " . ($r->isDefault() ? 'true' : 'false') . "\n";

$r = new ReflectionProperty(new foo, 'z');
echo "Foo::\$z isDefault: " . ($r->isDefault() ? 'true' : 'false') . "\n";

--EXPECT--
Foo::$x isDefault: true
Foo::$y isDefault: true
Foo::$z isDefault: false
