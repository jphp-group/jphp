--TEST--
Error case: class constant as an array
--FILE--
<?php
  class myclass
  {
      const myConst = array(1, 2, 3);
  }

  var_dump(myclass::myConst);
?>
--EXPECTF--
array(3) {
  [0]=>
  int(1)
  [1]=>
  int(2)
  [2]=>
  int(3)
}