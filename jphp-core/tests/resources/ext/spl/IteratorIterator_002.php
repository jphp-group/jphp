--TEST--
IteratorIterator with iterator
--FILE--
<?php

use php\util\Flow;

$it = new IteratorIterator(Flow::ofRange(1, 3));

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
int(0)

int(2)
int(1)

int(3)
int(2)