<?php

class MM {

    public function __isset($property){
        var_dump($property);
        return false;
    }
}

$mm = new MM();
var_dump(empty($mm->prop));