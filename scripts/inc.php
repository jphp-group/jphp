<?php

class Test {

    protected function n($i){
    }

    function call(){
        for($i = 0; $i < 10000; $i++){
            $this->n($i);
        }
    }
}

$t = new Test();
for($i = 0; $i < 1000; $i++)
    $t->call();