--TEST--
Test typed properties error condition (type mismatch object)
--FILE--
<?php
class Dummy {}
class Foobar {
    public stdClass $std;

    public function __construct(Dummy $dummy) {
        $this->std = $dummy;
    }
}

new Foobar(new Dummy);
?>
--EXPECTF--

Fatal error: Uncaught TypeError: Cannot assign Dummy to property Foobar::$std of type stdClass in %s on line %d, position %d
Stack Trace:
#0 Foobar->__construct() called at %s
#1 {main}
  thrown in %s on line %d
