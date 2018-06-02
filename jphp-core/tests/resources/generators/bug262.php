--TEST--
Bug #262: Invalid ref behaviour in generators.
--FILE--
<?php

function &gen() {
    $value = 5;

    while ($value > 0) {
        yield $value;
    }
}

$gen = gen();

for ($i = 0; $i < 5; $i++) {
    $gen->next();
    $a = &$gen->current();
    var_dump($a--);
}

?>
--EXPECT--
int(5)
int(4)
int(3)
int(2)
int(1)