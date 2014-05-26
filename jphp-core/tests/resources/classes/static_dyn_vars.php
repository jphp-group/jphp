--TEST--
Test getting static vars via expressions
--FILE--
<?php

class a {
    static $foo = 'bar';

    static function b1() {
        $varvar = 'foo';
        $foo = 'foo';
        return static::$$$varvar;
    }

    static function b2() {
        $varvar = 'foo';
        return static::$$varvar;
    }

    static function b3() {
        $varvar = 'foo';
        return static::${$varvar};
    }

    static function b4() {
        return static::${'foo'};
    }
}

var_dump(a::b1());
var_dump(a::b2());
var_dump(a::b3());
var_dump(a::b4());
?>
--EXPECT--
string(3) "bar"
string(3) "bar"
string(3) "bar"
string(3) "bar"
