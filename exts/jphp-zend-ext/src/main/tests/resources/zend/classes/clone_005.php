--TEST--
ZE2 object cloning, 5
--FILE--
<?php
abstract class base {
  public $a = 'base';

  // disallow cloning once forever
  final private function __clone() {}
}

class test extends base {
  // reenabling should fail
  public function __clone() {}
}

?>
--EXPECTF--

Fatal error: Cannot override final method base::__clone() with test::__clone() in %s on line %d, position %d