--TEST--
Invalid use: underscore next to 0x
--FILE--
<?php
0x_0123;
--EXPECTF--

Parse error: Syntax error, unexpected '_' in %s on line 2, position 3