<?php

class C implements I {
    function f($a = 2) {
        var_dump($a);
    }
}

$c = new C;
$c->f();