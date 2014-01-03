--TEST--
ZE2 __call() signature check
--SKIPIF--
<?php if (version_compare(zend_version(), '2.0.0-dev', '<')) die('skip ZendEngine 2 needed'); ?>
--FILE--
<?php

class Test {
    function __call() {
    }
}

?>
--EXPECTF--

Fatal error: Declaration of Test::__call() must be compatible with Object::__call($name, $arguments) in %s on line %d, position %d
