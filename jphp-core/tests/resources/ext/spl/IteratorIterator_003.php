--TEST--
IteratorIterator with generator
--FILE--
<?php

function gen() {
    for ($i = 1; $i < 4; $i++) {
        yield $i;
    }
}

$it = new IteratorIterator(gen());

foreach ($it as $key => $el) {
    var_dump($el);
    var_dump($key);
    echo "\n";
}

?>
--EXPECT--
int(1)
int(0)

int(2)
int(1)

int(3)
int(2)