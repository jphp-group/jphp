<?php

trait two {
    public $x = parent::X;
}

trait one {
}

class P {
    const X = 10;
}

class MyCls extends P {
    use two, one;

    const X = 20;
}

$c = new MyCls();
var_dump($c);