<?php
namespace Foo;

function test() {
    $a = 'a';
    $b = 'b';
    $c = $a ^ ord($b);
}

test();