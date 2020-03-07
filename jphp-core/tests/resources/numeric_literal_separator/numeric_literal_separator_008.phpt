--TEST--
Invalid use: underscore left of e
--FILE--
<?php
1_e2;
--EXPECTF--

Parse error: Syntax error, unexpected '_' in %s on line 2, position 2