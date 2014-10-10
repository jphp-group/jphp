--TEST--
Generator::throw() on an already closed generator
--FILE--
<?php
function gen() {
    yield;
}
$gen = gen();
$gen->next();
$gen->next();
var_dump($gen->valid());

try {
    $gen->throw(new Exception('test'));
} catch (Exception $e) {
    echo "Exception: ", $e->getMessage();
}
?>
--EXPECTF--
bool(false)
Exception: test