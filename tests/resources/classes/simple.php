<?php

class Simple {
    var $x = 10; var $y = 10;

    function test(){
        return 20;
    }

    static function staticTest(){
        return 30;
    }
}

$simple = new Simple();
return $simple->test() + Simple::staticTest() + $simple->staticTest()
    + $simple->x + $simple->y === 100 ? 'success' : 'fail';
