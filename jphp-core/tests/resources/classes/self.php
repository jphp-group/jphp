<?php

class Bar {
    static function func2(){
        return 30;
    }

    static function func3(){
        return 30;
    }
}

class Foo extends Bar {
    static function func2(){
        return 20;
    }

    static function func1(){
        return self::func2() + self::func3();
    }

    static function get(){
        return new self();
    }
}

if (Foo::func1() !== 50)
    return 'fail_foo_func1';

$foo = foo::get();
if ($foo->func1() !== 50)
    return 'fail_get';

return 'success';
