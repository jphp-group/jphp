--TEST--
Test typed properties error condition (fetch uninitialized by reference)
--FILE--
<?php
class Foobar {
    public int $int;
}

$thing = new Foobar();

$var = &$thing->int;
?>
--EXPECTF--

Fatal error: Typed property Foobar::$int must not be accessed before initialization in %s on line %d, position %d