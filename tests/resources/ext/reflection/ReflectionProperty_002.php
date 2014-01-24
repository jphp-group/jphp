--TEST--
Reflection Property -> is ?
--FILE--
<?php

class Foo {
    protected static $static;
    private var $dynamic;
}

$r = new ReflectionProperty('Foo', 'static');
echo "Foo::\$static is static: " . ($r->isStatic() ? 'true' : 'false') . "\n";
echo "Foo::\$static is protected: " . ($r->isProtected() ? 'true' : 'false') . "\n";
echo "Foo::\$static is public: " . ($r->isPublic() ? 'true' : 'false') . "\n";
echo "Foo::\$static is private: " . ($r->isPrivate() ? 'true' : 'false') . "\n";

echo "\n";
$r = new ReflectionProperty('Foo', 'dynamic');
echo "Foo::\$dynamic is static: " . ($r->isStatic() ? 'true' : 'false') . "\n";
echo "Foo::\$dynamic is protected: " . ($r->isProtected() ? 'true' : 'false') . "\n";
echo "Foo::\$dynamic is public: " . ($r->isPublic() ? 'true' : 'false') . "\n";
echo "Foo::\$dynamic is private: " . ($r->isPrivate() ? 'true' : 'false') . "\n";

--EXPECTF--
Foo::$static is static: true
Foo::$static is protected: true
Foo::$static is public: false
Foo::$static is private: false

Foo::$dynamic is static: false
Foo::$dynamic is protected: false
Foo::$dynamic is public: false
Foo::$dynamic is private: true
