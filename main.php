<?php

class MyClass {

    public static function test() {
        $i = 0;
        $str = 'foobar';
        while(($i += 1) < 30000000) {
            $d = (20 + 33) . $str;
        }
    }
}
