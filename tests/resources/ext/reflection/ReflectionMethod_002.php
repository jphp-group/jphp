--TEST--
Reflection Method -> is ?
--FILE--
<?php

abstract class Foo {
    private function test1(){}
    protected function test2(){}
    public function test3(){}
    static function test4(){}
    final function test5(){}
    abstract function test6();
}

echo "Foo::test1 is private: " . (new ReflectionMethod('Foo', 'test1')->isPrivate()), "\n";
echo "Foo::test2 is protected: " . (new ReflectionMethod('Foo', 'test2')->isProtected()), "\n";
echo "Foo::test3 is public: " . (new ReflectionMethod('Foo', 'test3')->isPublic()), "\n";
echo "Foo::test4 is static: " . (new ReflectionMethod('Foo', 'test4')->isStatic()), "\n";
echo "Foo::test5 is final: " . (new ReflectionMethod('Foo', 'test5')->isFinal()), "\n";
echo "Foo::test5 is abstract: " . (new ReflectionMethod('Foo', 'test6')->isAbstract()), "\n";

--EXPECTF--
Foo::test1 is private: 1
Foo::test2 is protected: 1
Foo::test3 is public: 1
Foo::test4 is static: 1
Foo::test5 is final: 1
Foo::test5 is abstract: 1