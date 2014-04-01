--TEST--
Error case: class constant as an encapsed containing a variable
--FILE--
<?php
  class myclass
  {
      const myConst = "$myVar";
  }
?>
--EXPECTF--

Fatal error: Expecting constant value for myclass::myConst in %s on line 4, position %d