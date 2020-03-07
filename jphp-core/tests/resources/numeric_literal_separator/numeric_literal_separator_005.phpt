--TEST--
Invalid use: underscore right of period
--FILE--
<?php
100._0;
--EXPECTF--

Parse error: Syntax error, unexpected '_' in %s on line 2, position 5