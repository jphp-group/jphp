<?php

class IFoo {
    function test($x){}
}

class Foo extends IFoo {
    function test($x){
        echo "$x\n";
    }
}

$foo = new Foo();
$foo->test(111);