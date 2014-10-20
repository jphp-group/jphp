--TEST--
Generator::throw() where the generator throws a different exception
--FILE--
<?php

function gen() {
    echo "before yield\n";
    try {
        yield;
    } catch (RuntimeException $e) {
        echo 'Caught: ', $e->getMessage(), "\n\n";

        throw new LogicException('new throw');
    }
}

$gen = gen();
try {
    var_dump($gen->throw(new RuntimeException('throw')));
} catch (LogicException $e) {
    echo 'Caught: ', get_class($e), ': ', $e->getMessage(), ' - ', $e->getLine();
}

?>
--EXPECTF--
before yield
Caught: throw

Caught: LogicException: new throw - 10