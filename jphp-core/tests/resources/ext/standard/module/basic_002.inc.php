<?php

class Foo {
    public static function bar() {
        var_dump('success_class');
    }
}

function foobar() {
    var_dump('success_function');
}

var_dump('fail');
