<?php

trait one {
    function foobar() { echo "A"; }
}

trait two {
    function foobar() { echo "B"; }
}

trait three {
    function foobar() { echo "C"; }
}

class A {
    use one, two, three

    {
        two::foobar insteadof one;
        one::foobar insteadof three;
    }
}

$x = new A;
$x->foobar();