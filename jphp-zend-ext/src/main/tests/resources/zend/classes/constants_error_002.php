--TEST--
Error case: class constant as an array
--FILE--
<?php
  class myclass
  {
      const myConst = array();
  }
?>
--EXPECTF--

Fatal error: Expecting constant value for myclass::myConst in %s on line 4, position %d