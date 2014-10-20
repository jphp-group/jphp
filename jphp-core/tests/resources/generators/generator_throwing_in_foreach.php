--TEST--
Exceptions throwing by generators during foreach iteration are properly handled
--FILE--
<?php
function gen() {
    throw new Exception("foo");
    yield; // force generator
}

try {
    foreach (gen() as $value) {

    }
} catch (Exception $e) {
    echo get_class($e), ": ", $e->getMessage();
}
?>
--EXPECT--
Exception: foo