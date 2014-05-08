--TEST--
Bug #107 https://github.com/jphp-compiler/jphp/issues/107
--FILE--
<?php

class A {
    public function parent() {
        echo __CLASS__;
    }
}

class B extends A {
    public function parent() {
        parent::parent();
    }
}

$b = new B();
$b->parent();
?>
--EXPECT--
A
