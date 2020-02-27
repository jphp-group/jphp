--TEST--
Test typed properties overflowing
--FILE--
<?php

$foo = new Foobar(); class Foobar {
    public int $bar = PHP_INT_MAX;
};

try {
    $foo->bar++;
} catch(TypeError $t) {
    var_dump($t->getMessage());
}

try {
    $foo->bar += 1;
} catch(TypeError $t) {
    var_dump($t->getMessage());
}

try {
    ++$foo->bar;
} catch(TypeError $t) {
    var_dump($t->getMessage());
}

?>
--EXPECTF--
string(76) "Cannot math add for property Foobar::$bar of type int past its maximal value"
string(76) "Cannot math add for property Foobar::$bar of type int past its maximal value"
string(76) "Cannot math add for property Foobar::$bar of type int past its maximal value"
