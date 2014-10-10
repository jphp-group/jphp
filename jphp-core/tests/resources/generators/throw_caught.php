--TEST--
Generator::throw() where the exception is caught in the generator
--FILE--
<?php

function gen() {
    echo "before yield\n";
    try {
        yield;
    } catch (RuntimeException $e) {
        echo $e->getMessage(), "\n\n";
    }

    yield 'result';
}

$gen = gen();
var_dump($gen->throw(new RuntimeException('Test')));

?>
--EXPECT--
before yield
Test

string(6) "result"
