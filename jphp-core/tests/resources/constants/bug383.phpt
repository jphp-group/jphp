--TEST--
Cannot concatenate strings or do other operations with constants when defining namespaced constants.
--FILE--
<?php
namespace a;

const BIN_DIR = PHP_INT_SIZE . '..' . PHP_INT_SIZE . 'bin';

var_dump(BIN_DIR);
?>
--EXPECT--
string(7) "8..8bin"