--TEST--
IteratorIterator with array
--FILE--
<?php

$it = new IteratorIterator([1 => 1, 2 => 2, 3 => 3]);

foreach ($it as $key => $el) {
    var_dump($el);
    var_dump($key);
    echo "\n";
}

foreach ($it as $el) {
    echo $el;
}

?>
--EXPECT--
int(1)
int(1)

int(2)
int(2)

int(3)
int(3)

123