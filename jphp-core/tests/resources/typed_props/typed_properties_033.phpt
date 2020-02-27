--TEST--
Test typed properties yield reference guard
--FILE--
<?php
class Foobar {
    public int $foo = 1;
    public int $bar = 3;
    public int $baz = 5;
    public int $qux = PHP_INT_MAX;

    public function &fetch() {
        yield $this->foo;
        yield $this->bar;
        yield $this->baz;
        yield $this->qux;
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

Fatal error: Unable to assign by ref for typed property Foobar::$foo, jphp will not support this feature in %s on line 9, position %d
