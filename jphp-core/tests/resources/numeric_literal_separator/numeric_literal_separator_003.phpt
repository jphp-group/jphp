--TEST--
Invalid use: adjacent underscores
--FILE--
<?php
10__0;
--EXPECTF--

Parse error: Syntax error, unexpected '_' in %s on line 2, position 3