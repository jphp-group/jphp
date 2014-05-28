--TEST--
Bug #126
--FILE--
<?php

class a {
    public function b() {
        $a = function() {
            c(function() use ($d, $e) {
            });
        };
    }
}

echo "success";
?>
--EXPECT--
success
