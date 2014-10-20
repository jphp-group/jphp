--TEST--
try { yield } finally { throw }
--FILE--
<?php
function foo($f, $t) {
    for ($i = $f; $i <= $t; $i++) {
        try {
            yield $i;
        } finally {
            throw new Exception("success");
        }
    }
}

try {
    foreach (foo(1, 5) as $x) {
        echo $x, "\n";
    }
} catch (Exception $e) {
    echo $e->getMessage();
}

?>
--EXPECTF--
1
success