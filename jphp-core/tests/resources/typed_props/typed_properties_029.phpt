--TEST--
Test typed properties respect strict types (on)
--FILE--
<?php
declare(strict_types=1);

class Foo {
    public int $bar;
}

$foo = new Foo;
$foo->bar = "1";
?>
--EXPECTF--

Fatal error: Uncaught TypeError: Cannot assign string to property Foo::$bar of type int in %s on line 9, position %d
Stack Trace:
#0 {main}
  thrown in %s on line 9