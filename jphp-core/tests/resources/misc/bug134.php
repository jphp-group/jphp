--TEST--
Bug #124
--FILE--
<?php

class a {
    static function b() {
        $c = function() {
            $d = empty($e->f);
            $f = isset($e->f);
            var_dump($d, $f);
        };
        $c();
    }
}

a::b();
?>
--EXPECT--
bool(true)
bool(false)
