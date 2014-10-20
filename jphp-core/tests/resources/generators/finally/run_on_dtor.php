--TEST--
finally is run on object dtor, not free
--FILE--
<?php
function gen() {
    try {
        yield;
    } finally {
        var_dump([]);
    }
}
$gen = gen();
$gen->rewind();

?>
--EXPECT--
array(0) {
}