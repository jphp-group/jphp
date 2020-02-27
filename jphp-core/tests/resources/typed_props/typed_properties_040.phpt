--TEST--
Test __get on unset typed property must fail properly
--FILE--
<?php
declare(strict_types=1);

class Foo {
    public int $bar;

    public function __get($name) {
        var_dump($name);
    }
}

$foo = new Foo();

unset($foo->bar);
var_dump($foo->bar);
?>
--EXPECTF--
string(3) "bar"

Fatal error: Uncaught TypeError: Cannot assign null to property Foo::$bar of type int in %s on line 15, position %d
Stack Trace:
#0 Foo->__get() called at %s
#1 {main}
  thrown in %s on line 15