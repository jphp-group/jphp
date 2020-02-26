--TEST--
Test typed properties yield reference guard
--FILE--
<?php
class Foobar {
    public int $foo = 1;
    public int $bar = 3;
    public int $baz = 5;
    //public int $qux = PHP_INT_MAX;

    public function &fetch() {
        yield $this->foo;
        yield $this->bar;
        yield $this->baz;
        //yield $this->qux;
    }
};
$foo = new Foobar;

try {
    foreach ($foo->fetch() as &$prop) {
        $prop += 1;
    }
} catch (Error $e) { echo $e->getMessage(), "\n"; }

var_dump($foo);
?>
--EXPECTF--
object(Foobar)#%d (3) {
  ["foo"]=>
  int(2)
  ["bar"]=>
  int(4)
  ["baz"]=>
  int(6)
}
