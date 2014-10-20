--TEST--
try { throw } finally { yield }
--FILE--
<?php
function foo($f, $t) {
    for ($i = $f; $i <= $t; $i++) {
        try {
            throw new Exception("success");
        } finally {
            yield $i;
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