<?php

class bar {

    static function call(){
        return static::test();
    }

    static function test(){
        echo "bar\n";
    }
}

class foo extends bar {
    static function test(){
        return 1;
    }
}


for($i = 0; $i < 10000000; $i++){
    $x = foo::call();
}
