--TEST--
Test typed properties return by ref is allowed
--FILE--
<?php
class Foobar {
    public int $bar = 15;

    public function &method() {
        return $this->bar;
    }
}

$foo = new Foobar();

var_dump($foo->method());
?>
--EXPECT--
int(15)
