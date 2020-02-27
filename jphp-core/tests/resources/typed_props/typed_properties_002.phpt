--TEST--
Test typed properties error condition (read uninitialized)
--FILE--
<?php
class Foobar {
    public int $int;
};
$thing = new Foobar();
var_dump($thing->int);
?>
--EXPECTF--

Fatal error: Uncaught Error: Typed property Foobar::$int must not be accessed before initialization in %s on line 6, position %d
Stack Trace:
#0 {main}
  thrown in %s on line 6