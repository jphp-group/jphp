<?php

class MyClass {

    public static function test(){
        for($i = 0; $i < 10000000; $i++){
            $x = ceil($i) + 2;
        }

        echo $x;
    }
}

MyClass::test();
