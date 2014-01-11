--TEST--
ZE2 __get() signature check
--SKIPIF--
<?php if (version_compare(zend_version(), '2.0.0-dev', '<')) die('skip ZendEngine 2 needed'); ?>
--FILE--
<?php
class Test {
        function __get($x,$y) {
        }
}

?>
--EXPECTF--

Fatal error: Declaration of Test::__get() must be compatible with Object::__get($property) in %s on line %d, position %d
