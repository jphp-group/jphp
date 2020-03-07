--TEST--
Invalid use: underscore left of period
--FILE--
<?php
100_.0;
--EXPECTF--

Parse error: Syntax error, unexpected '_' in %s on line 2, position 4