--TEST--
ZE2 iterators cannot implement Traversable alone
--SKIPIF--
<?php if (version_compare(zend_version(), '2.0.0-dev', '<')) die('skip ZendEngine 2 needed'); ?>
--FILE--
<?php

class test implements Traversable {
}

$obj = new test;

foreach($obj as $v);

print "Done\n";
/* the error doesn't show the filename but 'Unknown' */
?>
--EXPECTF--

Fatal error: Cannot use system class/interface Traversable for test in %s on line %d, position %d