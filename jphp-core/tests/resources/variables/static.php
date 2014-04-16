<?php

function test(){
    static $i = 0;
    $i++;
    return $i;
}

class Foo {

    static function test(){
        static $i = 0;
        $i++;
        return $i;
    }
}

class Bar extends Foo {

    static function test(){
        static $i;
        $i++;
        return $i;
    }
}

class Simple {

    function test(){
        static $i;
        $i++;
        return $i;
    }
}

test();
test();
Foo::test();
Foo::test();
Bar::test();
Bar::test();

$simple = new Simple();
$simple->test();
$simple->test();

static $one = 20, $two, $three = 40;

if ($one !== 20)
    return 'fail_1';

if ($two !== null)
    return 'fail_2';

if ($three !== 40)
    return 'fail_3';


return test() + Foo::test() + Bar::test() + $simple->test() === 12 ? 'success' : 'fail';
