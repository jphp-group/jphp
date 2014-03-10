<?php

class Foo {
    static function call(){
        return 'fail';
    }

    static function test(){
        return static::call();
    }

    static function get(){
        return new static();
    }
}

class Bar extends Foo {
    static function call(){
      return 'success';
    }
}

$bar = Bar::get();
if (!($bar instanceof Bar))
    return 'fail_get';

return Bar::test();

