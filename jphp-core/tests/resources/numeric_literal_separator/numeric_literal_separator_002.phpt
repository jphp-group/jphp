--TEST--
Invalid use: trailing underscore
--FILE--
<?php
100_;
--EXPECTF--

Parse error: Syntax error, unexpected '_' in %s on line 2, position 4