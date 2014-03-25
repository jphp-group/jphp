<?php

trait two {

    public function test(){
        echo __TRAIT__;
    }
}


class MyCls {
    use two;
}

$c = new MyCls();
$c->test();