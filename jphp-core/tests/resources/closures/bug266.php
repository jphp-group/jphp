--TEST--
Bug #266
--FILE--
<?php

function test() {
    $data = null;

    $fn = function () use ($data) {
        $data['x'] = 'ddd';
        var_dump("success");
    };

    $fn();
}

test();
?>
--EXPECT--
string(7) "success"