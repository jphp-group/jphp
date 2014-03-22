<?php

trait one {
    function test() { echo "ura"; }
}

trait two {
    use one;

    function test() {
        echo "ura2";
    }
}


class MyCls {
    use two;
}

$x = new MyCls();
$x->test();