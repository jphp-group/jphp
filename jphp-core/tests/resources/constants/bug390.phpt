--TEST--
Cannot use private constant when initializing class properties
--FILE--
<?php

class a
{
    private const test = 'a';
    private $test = self::test;
    public function __construct() { var_dump($this->test); }
}

new a;
?>
--EXPECTF--
string(1) "a"