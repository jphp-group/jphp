--TEST--
Test typed properties var_dump uninitialized
--FILE--
<?php
$foo = new Foobar(); class Foobar {
    public int $bar = 10, $qux;
};

var_dump($foo);
?>
--EXPECTF--
object(Foobar)#%d (1) {
  ["bar"]=>
  int(10)
  ["qux"]=>
  uninitialized(int)
}
