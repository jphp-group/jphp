<?php

class My {

    public function test(){
    }
}

$mm = new My();
$method = 'ceil';

for($i = 0; $i < 10000000; $i++){
    ceil($i);
}