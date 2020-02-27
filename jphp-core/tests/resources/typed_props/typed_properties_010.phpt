--TEST--
Test typed properties allow fetch reference
--FILE--
<?php
class Foo {
    public int $bar = 1;
}

$cb = function(int &$bar) {
    var_dump($bar);
};

$foo = new Foo();
$cb($foo->bar);
?>
--EXPECTF--

Fatal error: Unable to pass int as ref argument, jphp will not support this feature in %s on line 11, position %d