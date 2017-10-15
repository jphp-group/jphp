--TEST--
Try .. Finally with catch
--FILE--
<?php

class A extends Exception {
}

class B extends Exception {
}

try {
    throw new A("a_success");
} catch (A | B $x) {
    echo($x->getMessage()), "\n";
} catch (Exception $e) {
    echo "a_fail";
}

try {
    throw new B("b_success");
} catch (A | B $x) {
    echo($x->getMessage()), "\n";
} catch (Exception $e) {
    echo "b_fail";
}

?>
--EXPECT--
a_success
b_success