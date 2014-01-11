--TEST--
Basic class support - attempting to create a reference to a class constant
--FILE--
<?php
  class aclass
  {
      const myConst = "hello";
  }

  echo "\nAttempting to create a reference to a class constant - should be parse error.\n";
  $a = &aclass::myConst;
?>
--EXPECTF--

Parse error: Syntax error, unexpected '=' in %s on line 8, position 6