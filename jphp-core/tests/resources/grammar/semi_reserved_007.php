--TEST--
Edge case: self::self, self::parent, parent::self semi reserved constants access
--FILE--
<?php

class Foo {
    const self = "self";
    const parent = "parent";
    public function __construct() {
        echo "From ", __METHOD__, ":", "\n";
        echo self::self, "\n";
        echo self::parent, "\n";
    }
}

class Bar extends Foo {
    public function __construct() {
        parent::__construct();
        echo "From ", __METHOD__, ":", "\n";
        echo parent::self, "\n";
        echo parent::parent, "\n";
    }
}

new Bar;

echo "\nDone\n";

?>
--EXPECTF--
From Foo::__construct:
self
parent
From Bar::__construct:
self
parent

Done