--TEST--
Invalid use: underscore right of e
--FILE--
<?php
1e_2;
--EXPECTF--

Parse error: Syntax error, unexpected '_' in %s on line 2, position 2