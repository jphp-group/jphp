--TEST--
Generators can't be serialized or unserialized
--FILE--
<?php
function gen() { yield; }
$gen = gen();
try {
    serialize($gen);
} catch (Exception $e) {
    echo $e->getMessage(), "\n\n";
}
try {
    var_dump(unserialize('O:9:"Generator":0:{}'));
} catch (Exception $e) {
    echo $e->getMessage(), "\n\n";
}
try {
    var_dump(unserialize('C:9:"Generator":0:{}'));
} catch (Exception $e) {
    echo $e->getMessage();
}
?>
--EXPECTF--
Serialization of 'Generator' is not allowed

Unserialization of 'Generator' is not allowed

Unserialization of 'Generator' is not allowed