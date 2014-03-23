<?php

trait two {

    public function test(){
        $this->x = 20;
    }
}


class MyCls {
    use two;
}

$c = new MyCls();
$c->test();
var_dump($c->x);