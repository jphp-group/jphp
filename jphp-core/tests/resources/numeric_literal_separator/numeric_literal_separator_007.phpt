--TEST--
Invalid use: underscore next to 0b
--FILE--
<?php
0b_0101;
--EXPECTF--

Parse error: Syntax error, unexpected '_' in %s on line 2, position 3