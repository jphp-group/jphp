--TEST--
Group use declaration list should not contain leading separator
--FILE--
<?php

use Foo\Bar\{\Baz};

?>
--EXPECTF--

Parse error: Syntax error, unexpected '\', expecting identifier or function or const in %s on line 3, position 14
