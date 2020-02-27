--TEST--
Test typed properties error condition (type mismatch)
--FILE--
<?php
class Foobar {
    public int $int;

    public function __construct(string $string) {
        $this->int = $string;
    }
}

new Foobar("PHP 7 is better than you, and it knows it ...");
?>
--EXPECTF--

Fatal error: Uncaught TypeError: Cannot assign string to property Foobar::$int of type int in %s on line 6, position %d
Stack Trace:
#0 Foobar->__construct() called at %s
#1 {main}
  thrown in %s on line 6