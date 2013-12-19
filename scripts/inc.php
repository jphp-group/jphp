<?php

class Test {

    protected function n($i){

    }

    function call(){
        for($i = 0; $i < 10000000; $i++){
            $this->n($i);
        }
    }
}

$t = new Test();
$t->call();