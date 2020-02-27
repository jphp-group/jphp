--TEST--
Test uninitialized typed properties normal foreach must not be yielded
--FILE--
<?php
class Foobar {
    public int $bar = 10, $qux;
};

$foo = new Foobar();

foreach ($foo as $key => $bar) {
    var_dump($key, $bar);
}
?>
--EXPECT--
string(3) "bar"
int(10)
