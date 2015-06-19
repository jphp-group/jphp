--TEST--
Test to check regressions on use statements and lexer state
--FILE--
<?php

use A\B\C\D;

class Foo
{
    private static $foo;

}

echo "\n", "Done", "\n";
?>
--EXPECTF--

Done