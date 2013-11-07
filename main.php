<?php

class MyClass {

    public function test() {
        while(($i+=1) < 10000000){
            $str = substr($i, 2);
        }
    }
}
