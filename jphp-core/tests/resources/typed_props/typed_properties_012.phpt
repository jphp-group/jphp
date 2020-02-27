--TEST--
Test typed properties allow fetch reference for foreach
--FILE--
<?php
class Foo {
    public int $bar = 1;
}

$foo = new Foo();
foreach ($foo as &$prop) {
    $prop++;
}
var_dump($foo);
?>
--EXPECTF--
object(Foo)#%d (1) {
  ["bar"]=>
  int(2)
}
