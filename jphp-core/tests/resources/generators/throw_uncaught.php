--TEST--
Generator::throw() where the exception is not caught in the generator
--FILE--
<?php

function gen() {
    yield 'thisThrows';
    yield 'notReached';
}

$gen = gen();
try {
    var_dump($gen->throw(new RuntimeException('test')));
} catch (RuntimeException $e) {
    echo $e->getMessage();
}

?>
--EXPECTF--
test