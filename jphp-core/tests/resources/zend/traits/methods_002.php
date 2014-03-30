--TEST--
Testing collision with magic methods
--FILE--
<?php

trait foo {
    public function __clone() {
        var_dump(__FUNCTION__);
    }
}

trait baz {
    public function __clone() {
        var_dump(__FUNCTION__);
    }
}

class bar {
    use foo;
    use baz;
}

$o = new bar;
var_dump(clone $o);

?>
--EXPECTF--

Fatal error: Trait method '__clone' has not been applied, because there are collision in 'baz' and 'foo' traits on bar in %s on line %d, position %d